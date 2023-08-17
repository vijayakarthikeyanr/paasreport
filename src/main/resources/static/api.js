var appPlatformList=null;
var api = "http://localhost:8080/api";
function getAppPlatformInfo() {
  $.ajax({
    url: api + "/getAppPlatformInfo",
    type: "GET",
    dataType: "json",
    success: function (data) {
      displayMSSummary(data);
    },
    error: function (request, message, error) {
      handleException(request, message, error);
    },
  });
}

function loadDataIntoTable() {
  var table =
    "<table id='myTable'><tr class='header'><th>Domain</th><th>Org</th>";
  table +=
    "<th>Application Instance</th><th>Technology Service Owner</th><th>Cl No</th><th>Environment</th><th>Microservices</th></tr>";
  appPlatformList.forEach((appPlatform) => {
	  var owner = appPlatform.technologyServiceOwner==null?"":appPlatform.technologyServiceOwner;
      table += "<tr><td>" + appPlatform.domain + "</td>";
      table += "<td>" + appPlatform.org + "</td>";
      table += "<td>" + appPlatform.applicationInstance + "</td>";
      table += "<td>" + owner + "</td>";
      table += "<td>" + appPlatform.clNo + "</td>";
      table += "<td>" + appPlatform.environment + "</td>";
      table += "<td>" + appPlatform.microservices + "</td></tr>";
  });
  table += "</table>";
  $("#applicationPlatformList").html(table);
  getDate();
}

function getDate() {
  var date = new Date();
  let day = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();
  var currentTime =
    date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
  let currentDate = `${day}-${month}-${year}`;
  document.getElementById("date").innerHTML = currentDate;
  document.getElementById("time").innerHTML = currentTime;
}

function displayMSSummary(data) {
  var arr=new Array();
  appPlatformList = data.appPlatformList;
  appPlatformList.forEach((appPlatform) => {
    if(!arr.includes(appPlatform.domain)){
      arr.push(appPlatform.domain);
    } 
  });
  createLink(arr);
}

function createLink(domainArr){
  var count = 0;
  var domainStr='';
  var domainCountArr = new Array();
  var table = "<table>";
  domainArr.forEach((domain) => {
    domainStr=domain;
    count=0;
    appPlatformList.forEach((appPlatform) => {
        if(domain===appPlatform.domain){
            count= count+appPlatform.microservices;
        }
    });
    table += "<tr><td>" + domain + "</td>";
    table += "<td>" + count + "</td></tr>";
    domainStr+=" "+count;
    domainCountArr.push(domainStr);
    domainStr="";
  });
  table += "</table>";
  $("#platformSummary").html(table);
  loadDataIntoTable();
}
