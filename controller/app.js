/****************************************************************************** 
* DEPENDENCIES 
******************************************************************************/
var express    = require('express')
var args       = require('command-line-args')
var bodyParser = require('body-parser')
var jsonFile   = require('jsonfile')


/****************************************************************************** 
* APP SETUP 
******************************************************************************/
var app = express()
app.use(bodyParser.json())


/****************************************************************************** 
* OPTIONS 
******************************************************************************/
const options = args([
  { name: 'port', alias: 'p', type: Number },
  { name: 'report', alias: 'r', type: String }
])

const port = options.port || 3000

const reportFilePattern = options.report || 'report.${botIp}.json'


/****************************************************************************** 
* HANDLERS 
******************************************************************************/
var fnLandingPage = function (req, res) {
  res.send('Hello World!')
}

var fnInit = function (req, res) {
	res.json(
		{'auth.user-agent': 'MyAwesomeBot'}
	)
}

var fnCommand = function (req, res) {
  res.json(
    {'ts': Date.now(), 'command':'KILL'}
  )
}

var fnReport = function (req, res) {
	var report = req.body
  var bip    = req.ip
  var file   = reportFilePattern.replace(/\$\{botIp\}/, bip)
  console.log('Received report from %s: %s', bip, report)
	jsonFile.writeFile(file, report, {spaces: 2})
  console.log('Report saved in %s', file);
	res.send('Report saved')
}


/****************************************************************************** 
* REST API 
******************************************************************************/
app.get('/', fnLandingPage)

app.get('/init', fnInit)

app.get('/command', fnCommand)

app.post('/report', fnReport)


/****************************************************************************** 
* START 
******************************************************************************/
app.listen(port, function () {
  console.log('Controller ready on port %d', port)
})
