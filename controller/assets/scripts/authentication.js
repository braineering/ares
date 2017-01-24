'use strict';

/*
 * Generates the Yaml Configuration File with inserted preferences.
 *
 * @return {Boolean} true if the operation is successful, false if not
 */
 function GenerateAuthentication() 
 {
  
  var oTable = document.getElementById('authentication_table');
  var rowsLength = document.getElementById('authentication_table_table').getElementsByTagName("tr").length;

  if(rowsLength <= 2){
    alert("Table of Authentication's fields is empty!");
    return false;
  }
  
  var auth = {}

  var i;
  for (i = 1; i < rowsLength; i++){

    var oCells = oTable.rows.item(i).cells;

    var cellLength = oCells.length;
    var name = oCells.item(0).innerHTML;
    var val = oCells.item(1).innerHTML;
    
    if(val)
      auth[name] = val;
  }

  var modalTitle = document.getElementById('response_title');
  var modalContainer = document.getElementById('response_body');
  modalContainer.textContent = JSON.stringify(auth, null, "  ");
  modalTitle.textContent = "Authentication:";

  $('#response').modal({ backdrop: 'static', keyboard: true, show: true });

  return true;
};