'use strict';

/**
 * Enables the Wait option for Command
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function WaitEnableDisable()
{
  var cmd = document.getElementById('command').value;

  if(cmd == "CALMDOWN" || cmd == "RESTART" || cmd == "KILL"){
    $('#wait_option').disabled = false;
    $('#div_wait_option').css("display","inherit");
  }
  else{
    $('#wait_option').checked = false;
    $('#wait_option').disabled = true;
    $('#div_wait_option').css("display","none");
  }

  if(cmd == "SLEEP"){
    $('#div_timeout_option').css("display","inherit");
    $('#div_set_timeout').css("display","inherit");
    
  }
  else{
    $('#div_timeout_option').css("display","none");
    $('#div_set_timeout').css("display","none");
  }

  if(cmd == "UPDATE")
    $('#div_set_properties').css("display","inherit");
  else
    $('#div_set_properties').css("display","none");

  return true;
};

/**
 * Enables the Delay option for Command
 *
 + @return {Boolean} true if the operation is successful
 */
function DelayEnableDisable(bEnable)
{
  if(bEnable){
    document.getElementById('cmd_delay').disabled = false;
    document.getElementById('cmd_delay_option').disabled = false;
    $('#div_delay_option').css("display","inherit");
  }
  else{
    document.getElementById('cmd_delay').disabled = true;
    document.getElementById('cmd_delay_option').disabled = true;
    $('#div_delay_option').css("display","none");
  }
  return true;
};

/**
 * Enables the Timeout option for Sleep Command
 *
 * @return {Boolean} true if the operation is successful
 */
function TimeoutEnableDisable(check)
{
  if(check){
    document.getElementById('sleep_timeout').disabled = false;
    document.getElementById('sleep_timeout_option').disabled = false;
  }
  else{
    document.getElementById('sleep_timeout').disabled = true;
    document.getElementById('sleep_timeout_option').disabled = true;
  }
  return true;
};

/**
 * Generates the Json file for the requested command
 *
 + @return {Boolean} true if the operation is successful
 */
function GenerateCommand() 
{

  event.preventDefault();
  
  var cmd = {};
  cmd.timestamp = Date.now();
  cmd.command = document.getElementById('command').value;
  var waitoption = document.getElementById('wait_option').checked;
  var delayoption = document.getElementById('delay_option').checked;
  var timeoutopt = document.getElementById('timeout_option').checked;
  var report = document.getElementById('report').checked;

  if(timeoutopt && cmd.command == "SLEEP"){
    var timeoutslider = document.getElementById('sleep_timeout').value;
    var timeoutunit = document.getElementById('sleep_timeout_option').value;
    var t = timeoutslider.split(';');
    cmd.timeout = t[0]+"-"+t[1]+":"+timeoutunit;
  }

  if(cmd.command == "UPDATE")
  {
    cmd.settings = {};

    var oTable = document.getElementById('properties_table');
    var rowsLength = document.getElementById('properties_table').getElementsByTagName("tr").length;

    if(rowsLength <= 2){
      alert("Table of Properties is empty!");
      return false;
    }

    var i;
    for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var name = oCells.item(0).innerHTML;
      var val = oCells.item(1).innerHTML;

      var name = oCells.item(0).innerHTML;
      var val = oCells.item(1).innerHTML;
      
      if(val)
        cmd.settings[name] = val;    
    }
  }

  if(cmd.command == "RESTART")
  {
    cmd.controller = {};
    cmd.controller.init = "init";
    cmd.controller.command = "cmd";
    cmd.controller.log = "log";
  }

  if(waitoption){
      var ds = cmd.command;
      if(ds == "CALMDOWN" || ds == "RESTART" || ds == "KILL")
        cmd.wait = true;
  }

  if(delayoption){
    var delayslider = document.getElementById('cmd_delay').value;
    var delayunit = document.getElementById('cmd_delay_option').value;
    if(delayunit != "select time unit")
    {
      var i = delayslider.split(';');
      cmd.delay = i[0]+"-"+i[1]+":"+delayunit;
    }
  }

  if(report)
    cmd.report = true;
  else
    cmd.report = false;

  var modalTitle = document.getElementById('response_title');
  var modalContainer = document.getElementById('response_body');
  modalContainer.textContent = JSON.stringify(cmd, null, "  ");
  modalTitle.textContent = "Command: ";

  $('#response').modal({ backdrop: 'static', keyboard: true, show: true });
  return true;
};