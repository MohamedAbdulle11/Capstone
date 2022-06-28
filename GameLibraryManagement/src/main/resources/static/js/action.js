$(document).ready(function () {
    $('#table_id').DataTable({
        "dom": "tp",
        "ordering": false,
        "pagingType": "simple",
        "lengthMenu": [10]
    });
});