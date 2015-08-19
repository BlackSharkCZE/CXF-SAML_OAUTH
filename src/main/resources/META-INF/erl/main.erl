#!/usr/bin/env escript


-define(SAML_ASSERTION_FILE, "/home/blackshark/tmp/spring-soap/src/main/resources/saml-assertion.xml").
-define(JSON_DATA_FILE, "/home/blackshark/tmp/spring-soap/src/main/resources/META-INF/erl/request.json").

-define(ACCESS_TOKEN,"eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0NDAwMTM3MzQsImF1ZCI6InR5cGVzcmVwbyIsImlzcyI6Imh0dHA6XC9cL2loczEubWV0YWlzMi5hbnguc2s6ODA4MVwvbWV0YWlzaWFtXC8iLCJqdGkiOiIwZmYyM2U0NC02OTRkLTQ5MmEtYTI1MC1iOGUxZWNjMGRmZjYiLCJpYXQiOjE0Mzk5ODQ5MzR9.uTvATGiQc8zhHGGI2CKPUslDuYGDGAfWCQnx7OS_qGFm3Tp0RZn6cjhD5tUcudo9ND9CDj0UwAFZLf1RlXyrtJ0g1toME3jnPb_TqzT0QxgPRc1YWkgwl6i9dGmrenZgSZkKAnbDNeQdZRHvb6ncnOFT7jxsdwjK6JKORPlAEDY").

-include("config.hrl").

main([String]) ->
  inets:start(),
  case String of
    "token" -> io:format("~p~n", [request:get_access_token(?DEFAULT_CLIENT_SECRET, ?DEFAULT_CLIENT_ID, ?DEFAULT_OIDC_TOKEN_URL)]);
    "rest_with_saml" -> io:format("~p~n", [request:call_rest_with_saml(?SAML_ASSERTION_FILE, ?JSON_DATA_FILE, false)]);
    "rest_with_saml_deflate" -> io:format("~p~n", [request:call_rest_with_saml(?SAML_ASSERTION_FILE, ?JSON_DATA_FILE, true)]);
    "rest_without_saml" -> io:format("~p~n", [request:call_rest_without_saml(?JSON_DATA_FILE)]);
    "rest_with_oauth" -> io:format("~p~n", [request:call_rest_with_oauth(?ACCESS_TOKEN, ?JSON_DATA_FILE)]);
    "rest_with_err_oauth" -> io:format("~p~n", [request:call_rest_with_oauth("123",?JSON_DATA_FILE)]);
    "deflate_to_file" -> io:format("~p~n", [request:deflate_data_to_file(?SAML_ASSERTION_FILE,"/tmp/assertion.deflated.b64")]);
    _ -> io:format("Unknow\n")
  end,
  inets:stop();

main(_) -> usage().


usage() ->
  io:format("usage: <token
  | rest_with_saml
  | rest_with_saml_deflate
  | rest_without_saml
  | rest_with_oauth
  | rest_with_err_oauth deflate_to_file>\n"),
  halt(1).
