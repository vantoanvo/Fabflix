// a function to get the id from url
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


//a function to handle the return data
function handleResult(resultData) {
    let movieTbodyElement = jQuery("#single_movie_tbody");

    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML += "<th>" + resultData[0]["movie_title"] + "</th>";
    rowHTML += "<th>" + resultData[0]["movie_year"] + "</th>";
    rowHTML += "<th>" + resultData[0]["movie_director"] + "</th>";
    rowHTML += "<th>" + resultData[0]["movie_genres"] + "</th>";
    rowHTML += "<th>" + resultData[0]["movie_rating"] + "</th>";
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