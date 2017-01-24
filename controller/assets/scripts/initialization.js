'use strict';

/*
 * Generates the Yaml Configuration File with inserted preferences.
 *
 * @return {Boolean} true if the operation is successful, false if not
 */
 function GenerateInitialization() 
 {
  
  event.preventDefault();

  var oTable = document.getElementById('initialization_table');
  var rowsLength = document.getElementById('initialization_table').getElementsByTagName("tr").length;

  if(rowsLength <= 2){
    alert("Table of Initialization's Property is empty!");
    return false;
  }
  
  var init = {}

  var i;
  for (i = 1; i < rowsLength; i++){

    var oCells = oTable.rows.item(i).cells;

    var cellLength = oCells.length;
    var name = oCells.item(0).innerHTML;
    var val = oCells.item(1).innerHTML;
    
    if(val)
      init[name] = val;
  }

  var modalTitle = document.getElementById('response_title');
  var modalContainer = document.getElementById('response_body');
  modalContainer.textContent = JSON.stringify(init, null, "  ");
  modalTitle.textContent = "Initialization:";

  $('#response').modal({ backdrop: 'static', keyboard: true, show: true });

  return true;
};