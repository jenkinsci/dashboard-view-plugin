let err = 0;
function repeat() {
  buildStat.getStats(function(t) {
    let tables = document.querySelectorAll(".dbv-agents-table");
    for (let table of tables) {
      let tableId = table.dataset.id;
      if (t.status === 200) {
        err = 0;
        let stats = t.responseObject();
        document.getElementById('agents-' + tableId).innerText = stats.agents;
        document.getElementById('onlineAgents-' + tableId).innerText = stats.onlineAgents;
        document.getElementById('offlineAgents-' + tableId).innerText = stats.offlineAgents;
        document.getElementById('disconnectedAgents-' + tableId).innerText = stats.disconnectedAgents;
        document.getElementById('tasksInQueue-' + tableId).innerText = stats.tasksInQueue;
        document.getElementById('runningJobs-' + tableId).innerText = stats.runningJobs;
      } else if (t.status === 503 || t.status === 0) {
        // Consider these recoverable and don't update error count
        document.getElementById('runningJobs-' + tableId).innerText = 'ERR-' +
          (t.status === 0)? 'OFFLINE' : 'PROXY';
      } else {
        err++;
        document.getElementById('runningJobs-' + tableId).innerText = 'ERR-' + err;
      }
    }

    if (err < 5) {
      setTimeout('repeat()', 2500);
    }
  });
}
repeat();
