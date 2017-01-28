'use strict';

/*
 * Generates the Yaml Configuration File with inserted preferences.
 *
 * @return {Boolean} true if the operation is successful, false if not
 */
 function GenerateConfiguration() 
 {
  event.preventDefault();
  
  var config = {};

  if(document.getElementById('cnfInfo').checked)
    config.cnfInfo = true;
  else
    config.cnfInfo = false;
  
  if(document.getElementById('tgtInfo').checked)
    config.tgtInfo = true;
  else
    config.tgtInfo = false;

  if(document.getElementById('sysInfo').checked)
    config.sysInfo = true;
  else
    config.sysInfo = false;

  if(document.getElementById('netInfo').checked)
    config.netInfo = true;
  else
    config.netInfo = false;
  
  config.controllers = new Array();

  var oTable = document.getElementById('controllers_table');
  var rowsLength = document.getElementById('controllers_table').getElementsByTagName("tr").length;

  if(rowsLength <= 2){
    alert("Table of Controllers is empty!");
    return false;
  }

  var i;
  for (i = 1; i < rowsLength; i++){

    var oCells = oTable.rows.item(i).cells;

    var cellLength = oCells.length;
    var controller = {};
    
    controller.init = oCells.item(0).innerHTML;
    controller.command = oCells.item(1).innerHTML;
    controller.log = oCells.item(2).innerHTML;

    if(controller.init != "")
      config.controllers.push(controller);
  }

  var pollingslider = document.getElementById('polling').value;
  var pollingunit = document.getElementById('polling_option').value;
  if(pollingunit != "select time unit")
  {
    var p = pollingslider.split(';');
    config.polling = p[0]+"-"+p[1]+":"+pollingunit;  
  }
  var recs = document.getElementById('reconnections').value;
  if(recs)
    config.reconnections = parseInt(recs, 10);
 
  var reconnectionsslider = document.getElementById('reconnectionWait').value;
  var reconnectionsunit = document.getElementById('reconnectionWait_option').value;
  if(reconnectionsunit != "select time unit")
  {
    var r = reconnectionsslider.split(';');
    config.reconnectionWait = r[0]+"-"+r[1]+":"+reconnectionsunit;  
  }
  var proxy = document.getElementById('proxy').value;
  if(proxy!="")
    config.proxy = proxy;
  else
    config.proxy = "none";

  var oTable = document.getElementById('authentication_table');
  var rowsLength = document.getElementById('authentication_table').getElementsByTagName("tr").length;

  if(rowsLength > 2){
    
    config.auth = {};

    var i;
    for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var name = oCells.item(0).innerHTML;
      var val = oCells.item(1).innerHTML;
      
      if(val)
        config.auth[name] = val;
    }
  }

  var sm = document.getElementById('sleep-mode').value;
  if(sm != "")
  {
    var regex = sm;
    config.sleep = regex;
  }

  var yamlString = YAML.stringify(config, null, 4 );
  
  var modalTitle = document.getElementById('responseconf_title');
  var modalContainer = document.getElementById('responseconf_body');
  var intro = "#===================================================\n# BOT Configuration file\n#===================================================\n";
  modalContainer.textContent = intro + yamlString
  modalTitle.textContent = "Configuration: ";

  $('#responseconf').modal({ backdrop: 'static', keyboard: true, show: true });
  return true;
};