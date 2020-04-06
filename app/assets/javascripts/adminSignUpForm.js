$(document).ready(function() {
    $("#password").keyup(function(eventTarget) {
        var strengthSpan = $("#passwordStrength");
        var score = zxcvbn(eventTarget.target.value).score;
        strengthSpan.html(zxcvbn(eventTarget.target.value).score);
        switch (score) {
            case 0:
            case 1:
            case 2:
                strengthSpan.attr('class', 'badge badge-danger');
                break;
            case 3:
                strengthSpan.attr('class', 'badge badge-warning');
                break;
            case 4:
                strengthSpan.attr('class', 'badge badge-success');
                break;
        }
    });
});
