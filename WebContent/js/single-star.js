// a function to get the id from url
function getParameterByName(target) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(target);
}


//a function to handle the return data
function handleResult(resultData) {
    let movieTbodyElement = jQuery("#single_star_tbody");

    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML += "<td>" + resultData[0]["star_name"] + "</td>";
    rowHTML += "<td>" + resultData[0]["birth_year"] + "</td>";
    //Add stars as hyperlink
    rowHTML += "<td>";
    for(let i = 0; i < resultData[0]["movies"].length; i++){
        rowHTML += "<a href='single-movie.html?id=" + resultData[0]["movies"][i]["movie_id"] + "'>" + resultData[0]["movies"][i]["movie_title"] + "</a>";
        if(i < resultData[0]["movies"].length - 1){
            rowHTML += ", ";
        }
    }
    rowHTML += "</td>";
    rowHTML += "</tr>";
    movieTbodyElement.append(rowHTML);
}

// Get id from URL
let starID = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
$.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-star?id=" + starID, // Setting request url, which is mapped by StarsServlet in StarsServlet.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});