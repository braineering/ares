'use strict';

/*
 * This function adds a new target in the "Attack Table." 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddTargetFormSubmit() {

  event.preventDefault();

  var add_target_form = document.getElementById('add-target-form');
  var data = formToJSON(add_target_form.elements);

  var table = document.getElementById("targets_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  var cell3 = row.insertCell(2);
  var cell4 = row.insertCell(3);
  var cell5 = row.insertCell(4);
  var cell6 = row.insertCell(5);
  var cell7 = row.insertCell(6);

  if(data["target"])
    cell1.innerHTML = data["target"];
  else
    cell1.innerHTML = "none";

  if(data["method"])
    cell2.innerHTML = data["method"];
  else
    cell2.innerHTML = "none";
  
  if(data["proxy"])
    cell3.innerHTML = data["proxy"];
  else
    cell3.innerHTML = "none";

  if(data["executions"])
    cell6.innerHTML = data["executions"];
  else
    cell6.innerHTML = "null";

  var oTable = document.getElementById('header_table');
  var rowsLength = document.getElementById('header_table').getElementsByTagName("tr").length;

  if(rowsLength <= 2){
    cell4.innerHTML = "none";
  }else
  {
    var header;
    var i;
    var x = false;
    for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var name = oCells.item(0).innerHTML;
      var val = oCells.item(1).innerHTML;
      var object = name+":"+val;
      if(val != ""){
        if(x == false){
          header = object;
          x = true
        }
        else
          header = header +" - "+object;
      }
    }
    cell4.innerHTML = header;
  }

  var oTable = document.getElementById('params_table');
  var rowsLength = document.getElementById('params_table').getElementsByTagName("tr").length;

  if(rowsLength <= 2){
    cell5.innerHTML = "none";
  }else
  {
    var params;
    var i;
    var x = false;
    for (i = 1; i < rowsLength; i++){

      var oCells = oTable.rows.item(i).cells;

      var cellLength = oCells.length;
      var name = oCells.item(0).innerHTML;
      var val = oCells.item(1).innerHTML;
      
      var object = name+":"+val;
      if(val != ""){
        if(x == false){
          params = object;
          x = true
        }
        else
          params = params +" - "+object;
      }
    }
  
    cell5.innerHTML = params;
  }

  if(data["period"] && data["period-option"]){
    var p = data["period"].split(';');
    if(data["period-option"] == "select time unit")
      cell7.innerHTML = "none";
    else  
      cell7.innerHTML = p[0]+"-"+p[1]+":"+data["period-option"];  
  }
  else
    cell7.innerHTML = "null";  

  //$('#add_target_modal').modal('toggle');
  $('#add_target_modal').on('hidden.bs.modal', function(e){
      $(this).removeData();
    });
};