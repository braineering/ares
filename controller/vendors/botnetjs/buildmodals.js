'use strict';

/*
 * Generates the information modal for the selected item
 * @return {Boolean} true if the operation is successful, false if not
 */
 function BuildModal(item) 
 {
  var Controllers = "List of controllers to contact. The default configuration has an empty list of controller and a code wired fallback controller data/samples/controllers/1/botinit,cmd,log.json.";
  var WaitDelayReportSleep ="DELAY: If delay is specified, the command will be executed after a random amount of time within the given interval; if not, the command will be executed immediately."
                           +" REPORT: If report is specified and set to true, the bot will send the report back to the controller, once the command has been executed. SLEEP: calendar for the"
                           +" sleeping mode. The default configuration has no sleep calendar. If a timeout si specified the command WAKEUP is internally invoked after a random amount of time within the specified interval.";
  var CommandsList ="CALMDOWN: all attacks are unscheduled. If wait is true, it waits for for termination of currently executing attacks; if false,"
                   +" it kills them immediately. KILL: the bot is killed, that is alla attacks are unscheduled and it transit to state. DEAD: for resource releasing. If wait is true, it waits for for"
                   +" termination of currently executing attacks; if false, it kills them immediately. REPORT: the bot sends the report to the controller. RESTART: all attacks are unscheduled,the bot"
                   +" transits to state INIT trying to contact the controller with resource as its init-interface. If wait is true, it waits for for termination of currently executing attacks; if false, it"
                   +" kills them immediately. SAVE-CONFIG: the currently loaded bot configuration is locally saved as default configuration. SLEEP. all attacks are suspended and the bot transits to state"
                   +" SLEEP. If a timeout si specified the command WAKEUP is internally invoked after a random amount of time within the specified interval. WAKEUP: if the bot is in state SLEEP, it lets scheduled"
                   +" attacks be able to fire again and transits to state EXECUTION.";
  var UserAgent ="The default User-Agent to use in HTTP interactions both with the command&control layer and targets.";
  var ProxyModal ="HTTP proxy through which the bot contacts remote controllers and targets. The default con- figuration has no proxy.";
  var ReconnectionWait ="Period between reconnections. The reconnection period is a random amount of time within a certain time interval. The period randomness guarantees a greater variance in bots behaviour.";
  var Reconnections ="Number of times the bot tries to connect to an unreacheable controller.";
  var Polling ="Period between pollings to controller for the next command to execute. The polling period is a random amount of time within a certain time interval. The period randomness guarantees a greater variance in bots behaviour.";
  var Spying ="Active log of the selected items.";


  var modalTitle = document.getElementById('generic_modal_title');
  var modalContainer = document.getElementById('generic_modal_body');
  
  if(item == "Controllers"){
    modalContainer.textContent = Controllers;
    modalTitle.textContent = "Controllers";
  }
  else if(item == "Reconnections"){
    modalContainer.textContent = Reconnections;
    modalTitle.textContent = "Reconnections";
  }
  else if(item == "CommandsList"){
    modalContainer.textContent = CommandsList;
    modalTitle.textContent = "Command List";
  }
  else if(item == "UserAgent"){
    modalContainer.textContent = UserAgent;
    modalTitle.textContent = "User Agent";
  }
  else if(item == "Proxy"){
    modalContainer.textContent = ProxyModal;
    modalTitle.textContent = "Proxy";
  }
  else if(item == "ReconnectionWait"){
    modalContainer.textContent = ReconnectionWait;
    modalTitle.textContent = "Reconnection Wait";
  }
  else if(item == "WaitDelayReportSleep"){
    modalContainer.textContent = WaitDelayReportSleep;
    modalTitle.textContent = "Wait - Delay - Report - Sleep";
  }
  else if(item == "Polling"){
    modalContainer.textContent = Polling;
    modalTitle.textContent = "Polling";
  }
  else if(item == "Spying"){
    modalContainer.textContent = Spying;
    modalTitle.textContent = "Spying";
  }
  else
    modalContainer.textContent = "Error in building modal";
  
  $('#generic_modal').modal({ backdrop: 'static', keyboard: true, show: true });
  return true;
};