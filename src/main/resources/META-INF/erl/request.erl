%%%-------------------------------------------------------------------
%%% @author blackshark
%%% @copyright (C) 2015, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 18. VIII 2015 9:16
%%%-------------------------------------------------------------------
-module(request).
-author("blackshark").

%% API
-export([send_request/2, send_request/4, get_access_token/2, get_access_token/3, get_access_token/0]).
-export([uncompress/1, compress/1, read_file/1]).
-export([deflate_data/1, deflate_data_to_string/1, deflate_data_to_file/2, file_to_base64_string/1]).
-export([call_rest_without_saml/0, call_rest_without_saml/1, call_rest_with_saml/3]).
-export([call_rest_with_oauth/0, call_rest_with_oauth/1, call_rest_with_oauth/2, call_rest_with_new_oauth/1]).

-export([call_soap_with_oauth/2]).
-on_load(init/0).

-define(GRANT_TYPE, "client_credentials").

-include("config.hrl").

% to run use: erl -pa ~/.erlang123/lib/mochiweb/ebin/

init() ->
  ok.

get_access_token() ->
  get_access_token(?DEFAULT_CLIENT_SECRET, ?DEFAULT_CLIENT_ID, ?DEFAULT_OIDC_TOKEN_URL).


call_soap_with_oauth(RequestFile, OAuthToken) ->
  case file:read_file(RequestFile) of
    {ok, Data} -> io:format("Using Token: ~p~n", [OAuthToken]),
      Header = {"Authorization", "Bearer " ++ OAuthToken},
      send_request(?DEFAULT_SOAP_ENDPOINT_URL, Data, [Header, {"accept", "application/xml"}], "application/xml");
    _ -> erlang:error("Can not read data file " ++ RequestFile)
  end.

call_rest_without_saml() ->
  send_request(?DEFAULT_REST_ENDPOINT_URL, []).

call_rest_without_saml(DataFile) ->
  case file:read_file(DataFile) of
    {ok, Data} -> send_request(?DEFAULT_REST_ENDPOINT_URL, Data, [{"Content-type", "application/json"}], "application/json");
    _ -> erlang:error("Can not read file " ++ DataFile)
  end.

call_rest_with_oauth() ->
  call_rest_with_oauth(get_access_token()).

call_rest_with_new_oauth(DataFile) ->
  call_rest_with_oauth(get_access_token(), DataFile).


call_rest_with_oauth(OAuthToken) ->
  io:format("Using Token: ~p~n", [OAuthToken]),
  Header = {"Authorization", "Bearer " ++ OAuthToken},
  send_request(?DEFAULT_REST_ENDPOINT_URL, [Header]).

call_rest_with_oauth(OAuthToken, DataFile) ->
  case file:read_file(DataFile) of
    {ok, Data} -> io:format("Using Token: ~p~n", [OAuthToken]),
      Header = {"Authorization", "Bearer " ++ OAuthToken},
      send_request(?DEFAULT_REST_ENDPOINT_URL, Data, [Header, {"accept", "application/json"}], "application/json");
    _ -> erlang:error("Can not read data file " ++ DataFile)
  end.


call_rest_with_saml(SamlTokenFile, DataFile, Deflate) ->
  Header = case Deflate of
             true -> {"Authorization", "SAML " ++ deflate_data_to_string(SamlTokenFile)};
             false -> {"Authorization", "SAML " ++ file_to_base64_string(SamlTokenFile)};
             _ -> erlang:error("Deflate must be true/false!")
           end,

  case file:read_file(DataFile) of
    {ok, Data} ->
      send_request(?DEFAULT_REST_ENDPOINT_URL, Data, [Header, {"accept", "application/json"}], "application/json");
    _ -> erlang:error("Can not load data file " ++ DataFile)
  end.


file_to_base64_string(FileName) ->
  case file:read_file(FileName) of
    {ok, Data} -> binary:bin_to_list(base64:encode(Data));
    _ -> erlang:error("Can not read file " ++ FileName)
  end.

deflate_data(FileName) ->
  case file:read_file(FileName) of
    {ok, Data} -> base64:encode(compress(Data));
    _ -> erlang:error("Can not read file " ++ FileName)
  end.

deflate_data_to_file(InputFile, OutputFile) ->
  file:write_file(OutputFile, deflate_data(InputFile)).

deflate_data_to_string(FileName) ->
  binary_to_list(deflate_data(FileName)).

read_file(FileName) ->
  {ok, Data} = file:read_file(FileName),
  Data.

uncompress(Data) ->
  Z = zlib:open(),
  zlib:inflateInit(Z),
  B1 = zlib:inflate(Z, Data),
  zlib:inflateEnd(Z),
  B1.

compress(Data) ->
  Z = zlib:open(),
  zlib:deflateInit(Z),
  B1 = zlib:deflate(Z, Data),
  B2 = zlib:deflate(Z, <<>>, finish),
  zlib:deflateEnd(Z),
  list_to_binary([B1, B2]).


get_access_token(ClientSecret, ClientID) ->
  get_access_token(ClientSecret, ClientID, ?DEFAULT_OIDC_TOKEN_URL).


get_access_token(ClientSecret, ClientID, OIDCUrl) ->
  Body = "grant_type=" ++ ?GRANT_TYPE ++ "&client_id=" ++ ClientID ++ "&client_secret=" ++ ClientSecret,
  Httpcrequest = httpc:request(post, {OIDCUrl, [], "application/x-www-form-urlencoded", Body}, [], []),
  case Httpcrequest of
    {ok, {{_, 200, _}, _, Response}} ->
      {struct, X} = mochijson:decode(Response),
      [H | _] = lists:map(fun({_, V}) -> V end, lists:filter(fun({N, _}) -> N == "access_token" end, X)),
      H;
    _ -> erlang:error(io:format("~p~n", [Httpcrequest]))
  end.

send_request(Url, Body, Headers, ContentType) ->
  SSLOpts = [{verify,verify_none},{depth,3}],
  case httpc:request(post, {Url, Headers, ContentType, Body}, [{ssl, SSLOpts}], []) of
    {ok, Rest} -> {Status, RHeaders, Response} = Rest,
      parse_response(Status, RHeaders, Response);
    {error, Rest} -> io:format("~p~n", [Rest]),
      erlang:error("Error send request!");
    _ -> erlang:error("Unknow error send request!")
  end.

send_request(Url, Headers) ->
  SSLOpts = [{verify,verify_none},{depth,3}],
  {CallStatus, {Status, RHeaders, Response}} = httpc:request(get, {Url, Headers}, [{ssl, SSLOpts}], []),
  case CallStatus of
    ok -> parse_response(Status, RHeaders, Response);
    _ -> erlang:error("Failed to call " + Url)
  end.

parse_response(Status, RHeader, Response) ->
  case Status of
    {_, 200, _} -> process_response_data(RHeader, Response);
    {_, 302, _} -> io:fwrite("Redirect\n");
    _ -> io:fwrite(lists:flatten(io_lib:format("~p", [Status])))
  end.

process_response_data(_, Response) ->
  Response.
