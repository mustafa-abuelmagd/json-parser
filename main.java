//json.parse
/*
* 1- parse string
* 2- lexer: tokenize string, i.e split by {, }, [, ], : etc
* 3- a function for each aspect (array, string , object, boolean, integer)
* 4-
* ----------------------
* operation
* 1- begin parsing
* 2- use lexer to get the next token
* 3- use a stack to keep track of nested objects and arrays
* 4- parse strings correctly
* 5- parse numbers correctly, integers and floating points
* 6- parse boolean and nulls
* 7- manage nested objects
*
*
*
* we tokenize the string into seperate tokens by splitting the json string by the limiters, [" ", "{", "}", ...]
* we iterate through the tokens array,
* for each case of tokens there's different logic; for "," if we're not in an array, the next is a key, etx
*
* */
