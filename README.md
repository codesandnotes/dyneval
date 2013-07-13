DynEval
=======

DynEval is a Concordion extension that allows "configuring" your specification documents by using references, as in Velocity documents. 

Basically, the DynEval extension parses your document in search of references, marked using ${ }, and replaces them with values. These values either come from a ".properties" file or are specified programmatically at fixture initialization. 

Why would one need to make a "dynamic" specification? 
In our case, it was a matter of delivering specifications that could be adapted to multiple clients: each client wanted to have proof that our application worked up to specs with its client-specific data set. So instead of writing different "versions" of the specs, we ended making the documents configurable. 

Feel free to read the very short and sweet documentation that comes with the extension. It has been written with the help of... Concordion, of course! 
