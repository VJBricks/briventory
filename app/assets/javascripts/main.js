/*jshint esversion: 6 */

/**
 * Adds a div above the element and display a spinner.
 * @param id the identifier of the element.
 */
function blockWithSpinner(id) {
    $(id).append(
        "<div class=\"spinner-over-div d-flex align-items-center justify-content-center\">\n" +
        "  <div class=\"spinner-border\" role=\"status\">\n" +
        "    <span class=\"visually-hidden\">Loading...</span>\n" +
        "  </div>\n" +
        "</div>");
}

/** Adds a class to the element for a specific duration. After the duration, the class is removed.
 *
 * @param id the identifier of the element.
 * @param newClass the new class to apply, then remove.
 * @param duration the duration of the appliance of the new class.
 */
function classTransition(id, newClass, duration) {
    $(id).addClass(newClass);
    setTimeout(function () {
        $(id).removeClass(newClass);
    }, duration);
}
