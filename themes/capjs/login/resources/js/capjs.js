document.addEventListener("DOMContentLoaded", function () {
  const widget = document.querySelector("#cap");
  if (widget) {
    widget.addEventListener("solve", function (e) {
      document.getElementById("cap-token").value = e.detail.token;
    });
  }
});
