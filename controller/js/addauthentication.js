'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddAuthenticationFormSubmit() {

  console.log("AddAuthenticationFormSubmit");

  event.preventDefault();

  var add_authentication_form = document.getElementById('add-authentication-form');
  var data = formToJSON(add_authentication_form.elements);

  var table = document.getElementById("authentication_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  
  if(data["authentication_name"])
    cell1.innerHTML = data["authentication_name"];
  else
    cell1.innerHTML = "none";
  
  if(data["authentication_value"])
    cell2.innerHTML = data["authentication_value"];
  else
    cell2.innerHTML = "none";

  $('#add_authentication_modal').modal('toggle');

  return true;
};