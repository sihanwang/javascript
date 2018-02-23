$(function () {

    var $orders = $('#orders');
    var $name = $('#name');
    var $drink = $('#drink');


    var orderTemplate=$('#order-template').html();

    function addOrder(order) {
        $orders.append(Mustache.render(orderTemplate, order));
    }
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/orders',
        success: function (orders) {
            $.each(orders, function (i, order) {
                addOrder(order);
            });
        },
        error: function () {
            alert('error loading orders');
        }

    });

    $('#add-order').on('click', function () {

        var order = {
            name: $name.val(),
            drink: $drink.val()
        };

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/orders',
            data: JSON.stringify(order),
            success: function(newOrder){
                addOrder(newOrder);
            },
            error: function() {
                alert('error saving orders');
            }
        })

    });

    $orders.delegate('.remove' , 'click', function() {

        var $li=$(this).closest('li');

        
        $.ajax ({
            type: 'DELETE',
            url: 'http://localhost:8080/api/orders/'+$(this).attr('data-id'),
            success: function ()
            {
                $li.fadeOut(300, function() {
                    $(this).remove();
                });
            }

        });        
    });

    $orders.delegate('.editOrder', 'click', function(){
        var $li=$(this).closest('li');
        $li.find('input.name').val($li.find('span.name').html());
        $li.find('input.drink').val($li.find('span.drink').html());
        $li.addClass('edit');
    });

    $orders.delegate('.cancelEdit', 'click', function(){
        var $li=$(this).closest('li');
        $li.removeClass('edit');
    });    

    $orders.delegate('.saveEdit', 'click', function(){
        var $li=$(this).closest('li');
        var order={
            name: $li.find('input.name').val(),
            drink: $li.find('input.drink').val()
        };
        $.ajax ({
            type: 'PUT',
            url: 'http://localhost:8080/api/orders/'+$li.attr('data-id'),
            data: JSON.stringify(order),
            success: function (newOrder)
            {
               $li.find('span.name').html(newOrder.name);
               $li.find('span.drink').html(newOrder.drink);
               $li.removeClass('edit');
            },
            error: function()
            {
                alert('error updating order');
            }

        });
    });  
});