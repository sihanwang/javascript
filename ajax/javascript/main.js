$(function (){

    var $orders=$('#orders');
    $.ajax({


        type: 'GET',
        url: 'http://localhost:8080/api/orders',
        success: function(orders){
            $.each(orders, function(i, order){
                $orders.append('<li>name: '+order.name+', drink: '+order.drink+'</li>');
                
            });

        }

    });

})