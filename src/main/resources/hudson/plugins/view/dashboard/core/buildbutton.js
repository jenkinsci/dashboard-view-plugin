// SPDX-FileCopyrightText: © 2023 TobiX <tobias-git@23.gs>
// SPDX-License-Identifier: MIT

Behaviour.specify(
  ".dashboard-view-job-portlet-build-button",
  "build-button",
  0,
  function (e) {
    var url = e.getAttribute("href");
    var message = e.getAttribute("data-notification");

    e.onclick = function () {
      fetch(url, {
        method: "post",
        headers: crumb.wrap({}),
      });
      hoverNotification(message, this, -100);
      return false;
    };
  }
);
