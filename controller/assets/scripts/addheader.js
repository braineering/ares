'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddHeaderFormSubmit() {

  event.preventDefault();
  var add_header_form = document.getElementById('add-header-form');
  var data = formToJSON(add_header_form.elements);

  console.log(data["header_name"]);
  console.log(data["header_value"]);
  
  var table = document.getElementById("header_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  
  if(data["header_name"])
    cell1.innerHTML = data["header_name"];
  else
    cell1.innerHTML = "none";
  
  if(data["header_value"])
    cell2.innerHTML = data["header_value"];
  else
    cell2.innerHTML = "none";

  $('#add_header_modal').modal('toggle');
  $('#add_target_modal').modal('show');
  return true;
};