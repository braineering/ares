'use strict';

/*
 *Extracts targets from the list and build the attack command.
 *
 * @return {Boolean} true if the operation is successful, false if not
 */
function GenerateAttack() 
{
   var oTable = document.getElementById('targets_table');
   var rowsLength = document.getElementById('targets_table').getElementsByTagName("tr").length;
   
   if(rowsLength <= 2){
   	alert("Table of Targets is empty!");
   	return false;
   }

   var attack = {};
   attack.command = "ATTACK_HTTP";
   attack.attacks = new Array();

   var i;
   for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var target = {};
		
	  target.method = oCells.item(1).innerHTML;
	  target.target = oCells.item(0).innerHTML;
	  target.proxy = oCells.item(2).innerHTML;
	  var ua = oCells.item(3).innerHTML;
	  target.properties = '{ '+ 'User-Agent : ' + ua + ' }';
	  target.executions = oCells.item(4).innerHTML;
      target.period = oCells.item(5).innerHTML;
      
      if(target.method != "")
      	attack.attacks.push(target);
  	}   

   var deley = document.getElementById('attack_delay').value;
   var delayunit = document.getElementById('attack_delay').value;
   var d = deley.split(';');
   attack.delay = d[0]+"-"+d[1]+":"+delayunit; 

   var report = document.getElementById('attack_report').checked;
   if(report)
   	attack.report = true;
   else
   	attack.report = false;

   var modalTitle = document.getElementById('response_title');
   var modalContainer = document.getElementById('response_body');
   modalContainer.textContent = JSON.stringify(attack, null, "  ");
   modalTitle.textContent = "Attack:";

   $('#response').modal({ backdrop: 'static', keyboard: true, show: true });

   return true;
};