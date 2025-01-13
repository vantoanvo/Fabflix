function submitLoginForm(formSubmitEvent) {
    formSubmitEvent.preventDefault();
    let submitButton = $('#submit_btn');
    submitButton.prop('disabled', true); // Disable the button to prevent multiple submissions
    submitButton.val('Logging in...'); // Change the button text to indicate the process
    $.ajax({
        url: 'api/login',
        method: 'POST',
        data: login_form.serialize(),
        success: (data) => {
            sessionStorage.setItem('email', data.email);
            handleLoginResult(data);  // Directly pass the data to the result handler
            // Re-enable the submit button and reset its text after processing
            submitButton.prop('disabled', false);
            submitButton.val('Submit'); // Reset button text
        },
        error: function () {
            console.log("ERROR IN LOGIN");
            $("#login_error_message").text("Server error occurred. Please try again later.").show();
        },
    });
}

function handleLoginResult(resultDataJson) {
    console.log("Handling login result: ", resultDataJson);  // Log the result
    if (resultDataJson.status === "success") {
        window.location.href = "top20movies.html";  // Change the redirect URL if necessary
    } else {
        $("#login_error_message").text(resultDataJson.message).show();
    }
}


let login_form = $("#login_form");
// Bind the submit action of the form to a handler function
login_form.submit(submitLoginForm);


