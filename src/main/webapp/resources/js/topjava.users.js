const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            // "stripeClasses": [],
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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

// function enabled() {
//     $("#checkbox").change(function() {
//        console.log($(this).closest('tr').attr("id"))
//     });
//
// }
$(function enabled() {
    $('#datatable').on('click', 'input[type=checkbox]', function () {
        $.ajax({
            url: ctx.ajaxUrl + $(this).closest('tr').attr("id"),
            type: 'POST',
            data: {
                isEnabled: $(this).is(":checked"),
            }
        }).done(ctx.ajaxUrl, function () {
            updateTable();
            successNoty("Updated");
        });
    });
});

