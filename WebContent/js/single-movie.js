// a function to get the id from url
function getParameterByName(target) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(target);
}


//a function to handle the return data
function handleResult(resultData) {
    let movieTbodyElement = jQuery("#single_movie_tbody");

    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML += "<td>" + resultData[0]["movie_title"] + "</td>";
    rowHTML += "<td>" + resultData[0]["movie_year"] + "</td>";
    rowHTML += "<td>" + resultData[0]["movie_director"] + "</td>";
    rowHTML += "<td>" + resultData[0]["movie_genres"] + "</td>";
    //Add stars as hyperlink
    rowHTML += "<td>";
    for(let i = 0; i < resultData[0]["stars"].length; i++){
        rowHTML += "<a href='single-star.html?id=" + resultData[0]["stars"][i]["star_id"] + "'>" + resultData[0]["stars"][i]["star_name"] + "</a>";
        if(i < resultData[0]["stars"].length - 1){
            rowHTML += ", ";
        }
    }
    rowHTML += "</td>";
    rowHTML += "<td>" + resultData[0]["movie_rating"] + "</td>";
    rowHTML += "</tr>";
    movieTbodyElement.append(rowHTML);
}

// Get id from URL
let movieID = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
$.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?id=" + movieID, // Setting request url, which is mapped by StarsServlet in StarsServlet.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});