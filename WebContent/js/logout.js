$(document).ready(function () {
    // Check if the user is logged in
    if (sessionStorage.getItem('email')) {
        // Display logout button if user is logged in
        $('#logout_btn').show();

        // Add event listener to the logout button
        $('#logout_btn').click(function () {
            // Make an AJAX call to log out
            $.ajax({
                url: 'api/logout',  // Endpoint to log the user out
                method: 'POST',
                success: function (data) {
                    // If logout is successful, clear session storage and redirect
                    sessionStorage.removeItem('email');  // Remove the email from session storage
                    window.location.href = 'login.html';  // Redirect to login page or homepage
                },
                error: function () {
                    alert("Logout failed. Please try again.");
                }
            });
        });
    } else {
        // Hide logout button if not logged in
        $('#logout_btn').hide();
    }
});
