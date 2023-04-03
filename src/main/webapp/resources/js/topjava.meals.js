const mealAjaxUrl = "admin/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            // "stripeClasses": [],
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function clearForm() {
    $('form[name=filterForm]').trigger('reset')
    updateTable();
}

function getBetween() {
    $.ajax({
        url: ctx.ajaxUrl + "filter",
        type: 'GET',
        data: {
            startDate: $('#startDate').val(),
            startTime: $('#startTime').val(),
            endDate: $('#endDate').val(),
            endTime: $('#endTime').val()
        }
    }).done(ctx.ajaxUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}