function getElementFromEvent(e) {
  var targ;
  if (e.target)
    targ = e.target;
  else if (e.srcElement)
    targ = e.srcElement;
  if (targ.nodeType == 3) // defeat Safari bug
    targ = targ.parentNode;
  return targ;
}

function myshow(e) {
  if (!e) var e = window.event;
  var receiver = getElementFromEvent(e);
  var elem = document.getElementById(receiver.id.substring(7));
  elem.style.visibility = 'visible';
  elem.style.display = '';
}

function myhide(e) {
  if (!e) var e = window.event;
  var receiver = getElementFromEvent(e);
  var elem = document.getElementById(receiver.id.substring(7));
  elem.style.visibility = 'hidden';
  elem.style.display = 'none';
}

function startsWith(str, substr) {
  return (str.match("^"+substr) == substr);
}

var cmds = window.document.getElementsByTagName('img');
for (var cmdIndex in cmds) {
  cmd = cmds[cmdIndex];
  if (cmd.id && startsWith(cmd.id, 'cmdExp-dashboard_portlet_')) {
    cmd.onclick = myshow;
  } else if (cmd.id && startsWith(cmd.id, 'cmdCol-dashboard_portlet_')) {
    cmd.onclick = myhide;
  }
}
