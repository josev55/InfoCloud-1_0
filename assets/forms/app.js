var xml = null;
var maxId = $("meta[property='maxid']").attr("content");
var xmlData = null;
var createXML = function () {
    $.get('info.xml', function (data) {
        xmlData = data;
        xml = $.get('template-copy' + data.find('lastCopy') + '.xml');
        xml.append("<" + data.find('id').text() + ">");
    });
}
$(document).ready(function () {
    createXML();
    var parsedXML = $.parseXML(xml);
    var ua = navigator.userAgent;
    if (ua.toLowerCase().indexOf("android") > -1) {
        $(".xdDTPicker").attr("onfocus", "openDateDialog()");
    }
    $(".xdDTPicker").focus(function () {
        AndroidFunction.openDatePickerDialog();
    });
});

function save() {
    for (var i = 0; i < maxId + 1; i++) {
        xml.append($("#" + i).val());
    }
    xml.append("</" + xmlData.find('id').text() + ">");
}

