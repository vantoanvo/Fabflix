//js file to two things:
// 1. use jquery to talk to the API to get the json data
// 2. populate data to correct html

function handleResult(resultData){
    console.log("handleResult: populating the result");
    //use jquery to select an html element
    let tableBody = jQuery("#top20-movies-tbody");
    //iterate through each row of the resultData
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<td>" + resultData[i]["title"] + "</td>";
        rowHTML += "<td>" + resultData[i]["year"] + "</td>";
        rowHTML += "<td>" + resultData[i]["director"] + "</td>";
        rowHTML += "<td>" + resultData[i]["genres"] + "</td>";
        rowHTML += "<td>" + resultData[i]["stars"] + "</td>";
        rowHTML += "<td>" + resultData[i]["rating"] + "</td>";
        rowHTML += "</tr>";
        tableBody.append(rowHTML);
    }
}

//use jquery to talk to the API
$.ajax({
    url: "api/top20-movies",
    method: "GET",
    dataType: "json",
    success: (resultData) => handleResult(resultData)
});