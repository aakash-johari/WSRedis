var express = require('express');
var app = express();
var path = require('path');

// viewed at http://localhost:8080
app.use('/', express.static('dist'))

app.listen(9090);