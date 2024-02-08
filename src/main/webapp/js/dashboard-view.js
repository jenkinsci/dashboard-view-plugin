function togglePortlet(portlet, show)
{
  let content = portlet.querySelector(".dbv-portlet__content");
  let collapse = portlet.querySelector(".dbv-portlet__title-button--collapse");
  let expand = portlet.querySelector(".dbv-portlet__title-button--expand");
  if (show) {
    content.classList.remove("jenkins-hidden");
    expand.classList.add("jenkins-hidden");
    collapse.classList.remove("jenkins-hidden");
  } else {
    content.classList.add("jenkins-hidden");
    expand.classList.remove("jenkins-hidden");
    collapse.classList.add("jenkins-hidden");
  }
}

Behaviour.specify(".dbv-portlet__title-button--maximize", "dbv-maximize", 0, function(cmd) {
    cmd.classList.remove("jenkins-hidden");
});

Behaviour.specify(".dbv-portlet__title-button--collapse", "dbv-collapse", 0, function(cmd) {
    let portlet = cmd.closest(".dbv-portlet");
    cmd.onclick = function() {
      togglePortlet(portlet, false);
    }
    cmd.classList.remove("jenkins-hidden")
});

Behaviour.specify(".dbv-portlet__title-button--expand", "dbv-expand", 0, function(cmd) {
    let portlet = cmd.closest(".dbv-portlet");
    cmd.onclick = function() {
      togglePortlet(portlet, true);
    }
});
