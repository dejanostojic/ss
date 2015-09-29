"use strict"
// TODO export module from global namespace!
var basketModule = (function () {
 
  // privates
 
  var module = this,
      basket = [],
      fullMap = {},
      $liElems = [],
      $cartViewElems = [],
      $menu = null,
      $viewCartContainer = null;

/*
 $( document ).on('added.dost.shop.basket', function(event){
 console.log('e: ' + JSON.stringify(event));   console.log('e data: ' + event.data);
 });

    $( document ).trigger({
      type:"added.dost.shop.basket",
      data: {user:"foo",
                 pass:"bar"}
    });
*/
    function getProductList(cartProducts){
        return Object.keys(cartProducts).map(function (key) {
            return cartProducts[key];
        });
    }

    function createProductMenuItem(product){
        return $( "<li></li>", {
          class: "product",
          'data-product-id': product.product.id,
          'data-variation-id': product.variation_id,
          'data-type-id': product.typeId,
          html: [$('<a></a>',{
                      href: "#",
                      class: 'img-product',
                      html: $('<figure><img src="http://placehold.it/100x100" alt=""/></figure>')
                  }),
                  $('<div></div>',{
                                   class: 'list-product',
                                   html: [$('<h5></h5>', {
                                                           html: product.product.name + '-' + (product.variation == null ? product.product.shop_product_type.name : product.variation.shop_variation.name)}),
                                           $('<div></div>', {
                                                           class: 'quantity',
                                                         html: product.quantity
                                                         }),
                                             $('<div></div>', {
                                               class: 'price-product',
                                             html: (product.product.price > 0 ? product.product.price : product.variation.price)
                                             }),
                                             $('<div class="remove-product" data-role="remove-product-from-cart"><i class="icon-close"></i></div>')]
                                                   })]
          });


    }

    function createViewCartRowItem(product){
        return $('<div></div>',{
            class: 'row cart-item',
            'data-role': 'full-cart-item',
            'data-product-id': product.product.id,
            'data-variation-id': product.variation_id,
            'data-formatted-date': product.formatted_date,
            'data-type-id': product.typeId,
            html: [$('<div></div>',{
                    class: 'col-xs-4 col-sm-5 product-name',
                    html: [$('<a href="product.html" class="product-thumbnail"><img src="http://placehold.it/100x100" class="" alt=""></a>'),
                            $('<a></a>',{
                                href: '#',
                                class: 'name',
                                html: product.product.name
                            }),
                            $('<span>' + product.product.shop_product_type.name +'</span>'),
                            $('<span>' + (product.variation != null ? product.variation.shop_variation.name : '') + '</span>')
                            ]
                    }),
                    $('<div></div>',{
                            class: 'col-xs-2 product-position product-price',
                            html: $('<span></span>', {
                                    class: 'amount',
                                    html: (product.product.price > 0 ? product.product.price : product.variation.price)})}),
                    $('<div></div>', {
                        class: 'col-xs-2 product-position product-quantity',
                        html: $('<div>',{
                                class: 'quantity',
                                html: [$('<input type="text" step="1" name="" title="Qty" value="' + product.quantity + '" class="input-text qty text" size="4">'),
                                        $('<input type="button" data-role="plus-quantity" value="+" class="plus hidden-xs">'),
                                        $('<input type="button" data-role="minus-quantity" value="-" class="minus hidden-xs">')]
                        })}),
                    $('<div></div>', {
                        class: 'col-xs-2 product-position product-subtotal',
                        html: $('<span></span>', {
                                                 class: 'amount',
                                                  html: ((product.product.price > 0 ? product.product.price : product.variation.price) * product.quantity)})
                    }),

                    $('<div></div>',{
                    class: 'col-xs-1 product-position product-remove',
                        html: $('<a href="#" class="remove" data-role="remove-product-from-cart" data-djax-exclude="true" title="Remove this item"><i class="icon-close"></i></a>')
                    })
                    ]
        });
    }


  function calculateAmount() {
    var sum = 0;
    $.each(basket,function(i, prod){
        sum = sum + prod.quantity * (prod.product.price > 0 ? prod.product.price : prod.variation.price);
    });
    return sum;
  }
  function getCartDropDownPrivate(){
          var $li = [];

          $.each(basket, function( index, value ) {
            $li.push(createProductMenuItem(value))
          });
          return $li;
      }

      function getCartViewElemsPrivate(){
        var $items = [];

        $.each(basket, function( index, value ) {
          $items.push(createViewCartRowItem(value))
        });
        return $items ;
      }

    function paint (){
         $liElems = getCartDropDownPrivate();
         $cartViewElems = getCartViewElemsPrivate();
         $menu.empty();
         $viewCartContainer.empty();

        $.each($liElems, function(index, value){
            value.appendTo($menu);
        });
        $.each($cartViewElems, function(index, value){
            value.appendTo($viewCartContainer);
        });


        $('<li></li>',{
            class: 'summation',
            html: [$('<div></div>',{
                    class: 'summation-subtotal',
                    html: [$('<span>subtotal</span>'),
                            $('<span></span>',{
                            class: 'amount',
                            html: calculateAmount()
                            })]
                    }),
                    $('<div class="btn-cart"><button class="btn btn-default" data-filter=".category-cart"><a href="#category-cart">Vidi korpu</a></button><button class="btn btn-dark"><a href="#category-checkout">Kupi</a></button></div>')]
        }).appendTo($menu);

$('<div>',{class: 'row cart-subtotal',
html: [$('<div class="col-xs-2 col-xs-offset-6 col-sm-offset-7 subtotal-txt"><span>Total:</span></div>'),
        $('<div class="col-xs-2 subtotal"><span class="amount">' + calculateAmount() + '</span></div>')]}).appendTo($viewCartContainer);

$('<div class="row cart-buttons"><div class="col-xs-12"><a href="#menu-meals" class="btn btn-dark  continue-shopping">Continue shopping</a><div class="product-btn"><a href="#" data-role="clear-shopping-cart" class="btn btn-default ">clear shopping cart</a></div></div></div>').appendTo($viewCartContainer);
$('<div class="row cart-buttons"><div class="col-xs-12"><div class="product-btn"><a href="#category-checkout" class="btn btn-color  ">Kupi</a></div></div></div>').appendTo($viewCartContainer);

    }

    function createMapKey(prodId, varId){
        return '{' + prodId + ' ' + varId +'}';
    }

    function getProduct(prodId, varId){
        return fullMap[createMapKey(prodId, varId)];
    }
 
  // Return an object exposed to the public
  return {
 
    // Add items to our basket
    addItem: function( values ) {
    //console.log(JSON.stringify(values));

        var prodid = values.product.id;
        var varid = values.variation_id;
        var key = '{' + prodid + ' ' + varid +'}';

        if (values.quantity == 0){
            delete fullMap[key];
        }else{
            fullMap[key] = values;
        }
        basket = getProductList(fullMap);
        paint();
    },

    fMap : function(){
        return fullMap;
    },


    init: function(cartProducts, elem, viewCartContainer){
        fullMap = cartProducts;
        basket = getProductList(fullMap);
        $menu = null;
        if (elem instanceof jQuery){
            $menu = elem;
        } else{
            $menu = $(elem);
        }

        $viewCartContainer = null;
        if (viewCartContainer instanceof jQuery){
            $viewCartContainer = viewCartContainer;
        } else{
            $viewCartContainer = $(viewCartContainer);
        }

        paint();

        $menu.on('click','[data-role="remove-product-from-cart"]',function(e){
            var $li = $(this).closest('li');
            var prodId = $li.attr('data-product-id');
            var varId = $li.attr('data-variation-id');

            $.ajax({
                      type: "POST",
                      url: '/dost/shop/cart',
                      data: {productId: prodId,
                             quantity:  0,
                             typeId: $li.attr('data-type-id'),
                             variationId: varId
                             },
                      dataType: 'json'
                    }).done(function( msg ) {
                        basketModule.addItem(msg)
                        console.log( "Data Saved: " + JSON.stringify(msg) );
                      }).fail(function( msg ) {
                        alert( "Data faild: " + JSON.stringify(msg) );
                      });

            });
            var that = this;

            $viewCartContainer.on('click', '[data-role="remove-product-from-cart"]', function(e){
                e.preventDefault();
                var $productItem = $(this).closest('[data-role="full-cart-item"]');
                var prodId = $productItem.attr('data-product-id');
                var varId = $productItem.attr('data-variation-id');
                var product = getProduct(prodId, varId);

                $.ajax({
                          type: "POST",
                          url: '/dost/shop/cart',
                          data: {productId: product.product.id,
                                 quantity:  0,
                                 typeId: product.type_id,
                                 variationId: product.variation_id
                                 },
                          dataType: 'json'
                        }).done(function( msg ) {
                            basketModule.addItem(msg)
                            console.log( "Data removed: " + JSON.stringify(msg) );
                          }).fail(function( msg ) {
                            alert( "Data faild: " + JSON.stringify(msg) );
                          });
            });

            $viewCartContainer.on('click', '[data-role="clear-shopping-cart"]', function(e){
                e.preventDefault();
                that.clear();
            });
            $viewCartContainer.on('click', '[data-role="plus-quantity"]', function(e){
                e.preventDefault();
                var $productItem = $(this).closest('[data-role="full-cart-item"]');
                var prodId = $productItem.attr('data-product-id');
                var varId = $productItem.attr('data-variation-id');
                var formattedDate = $productItem.attr('data-formatted-date');
                var product = getProduct(prodId, varId);

                $.ajax({
                          type: "POST",
                          url: '/dost/shop/cart',
                          data: {productId: prodId,
                                 quantity:  product.quantity + 1,
                                 typeId: product.type_id,
                                 variationId: product.variation_id,
                                 formattedDate: formattedDate
                                 },
                          dataType: 'json'
                        }).done(function( msg ) {
                            basketModule.addItem(msg)
                            console.log( "Data increased: " + JSON.stringify(msg) );
                          }).fail(function( msg ) {
                            alert( "Data faild: " + JSON.stringify(msg) );
                      });
            });
            $viewCartContainer.on('click', '[data-role="minus-quantity"]', function(e){
                e.preventDefault();
                var $productItem = $(this).closest('[data-role="full-cart-item"]');
                var prodId = $productItem.attr('data-product-id');
                var varId = $productItem.attr('data-variation-id');
                var formattedDate = $productItem.attr('data-formatted-date');
                var product = getProduct(prodId, varId);
                $.ajax({
                          type: "POST",
                          url: '/dost/shop/cart',
                          data: {productId: prodId,
                                 quantity:  product.quantity - 1,
                                 typeId: product.type_id,
                                 variationId: product.variation_id,
                                 formattedDate: formattedDate
                                 },
                          dataType: 'json'
                        }).done(function( msg ) {
                            basketModule.addItem(msg)
                            console.log( "Data decreased: " + JSON.stringify(msg) );
                          }).fail(function( msg ) {
                            alert( "Data faild: " + JSON.stringify(msg) );
                      });
            });


    },

    getCartDropDown: getCartDropDownPrivate,

    paint: paint,

    replace: function(){
        $.each($liElems, function(index, value){
                value.replaceWith($liElems[0]);
                //value.insertBefore($('ul.show-cart > li.summation'));
            })
    },
    print: function(){
        console.log('full map: ' + JSON.stringify(fullMap));
        console.log('basket: ' + JSON.stringify(basket));
    },

    fullMap: function(){return fullMap;},

    // Get the count of items in the basket
    getItemCount: function () {
      return basket.length;
    },

    // clear basket
    clear: function(){
        $.ajax({
         type: "DELETE",
         url: '/dost/shop/cart',
         data: {},
         dataType: 'json'
       }).done(function(){
       console.log('deleted');
           fullMap = {};
           basket = [];
           paint();
       });
    },

 
    // Get the total value of items in the basket
    getTotal: function () {
 
      var q = this.getItemCount(),
          p = 0;
 
      while (q--) {
        p += basket[q].price;
      }
 
      return p;
    }
  };
})();

// Global namespace! Dirty hack! TODO crete module
var loggedInUserData = null;

function addAntiForgery(antiForgeryTocken){
	$( document ).ajaxSend(function( event, jqxhr, settings ) {
		jqxhr.setRequestHeader("x-csrf-token", antiForgeryTocken);
	});
}

function setLoggedInUserData(userData){
	loggedInUserData = userData;
}


function fillFormWithUserData($form,userData){
         if (!($form instanceof jQuery)){
            $form = jQuery($form);
         }
         $form.find('input[name="first_name"]').val(userData.first_name);
         $form.find('input[name="last_name"]').val(userData.last_name);
         $form.find('input[name="email"]').val(userData.email);
         $form.find('input[name="phone"]').val(userData.phone);
         $form.find('input[name="shipping_address"]').val(userData.shipping_address);
     }

(function($){

    $('[data-role="show-item-action"]').on('click',function(e){
        e.preventDefault();
        $(this).closest('[data-role="menu-item"]').find('[data-role="item-action"]').toggleClass('hide');
    });
	$('div[data-role="add-to-cart"]').find('a[data-role="shop-product-variation-button"]').on('click',function(e){
        e.preventDefault();
        var $this = $(this);

        var formattedDate = $this.attr('data-formatted-date') || $this.closest('[data-role="menu-item"]').find('select[data-role="date-select"]').val();

        $.ajax({
          type: "POST",
          url: '/dost/shop/cart',
          data: {productId: $this.attr('data-product-id'),
                 quantity:  $this.prev().val(),
                 typeId: $this.attr('data-type-id'),
                 variationId: $this.attr('data-variation-id'),
                 formattedDate: formattedDate
                 },
          dataType: 'json'
        }).done(function( msg ) {
            basketModule.addItem(msg);
            var $popInfo = $('<div class="btn-cart"><a class="btn btn-default" href="#category-cart" data-role="show-cart-and-close-popover">Vidi korpu</a><a class="btn btn-dark" data-role="checkout-and-close-popover" href="#category-checkout">Kupi</a></div>');
            $popInfo.find('a[data-role="show-cart-and-close-popover"]').click(function(){$this.webuiPopover('hide');});
            $popInfo.find('a[data-role="checkout-and-close-popover"]').click(function(){$this.webuiPopover('hide');});
            $this.webuiPopover({trigger:'click', cache: false, title: "Dodato u korpu", closeable: true, content: $popInfo})
            $this.webuiPopover('show');
            console.log( "Data Saved: " + JSON.stringify(msg) );
          }).fail(function( msg ) {
            alert( "Data faild: " + JSON.stringify(msg) );
          });
	});

	$('form[data-role="order-form"]').on('submit',function(e){
        e.preventDefault();
        var $form = $(this);
        var formData = $form.serialize();
        $.ajax({
            type: "POST",
            url: '/dost/shop/order',
            data: formData,
            dataType: 'json'
          }).success(function(msg){
              var $createAcount = '<form method="POST" data-role="after-order-register" action="/dost/register/after-order"><p>Želite da naručite brže? Unesite šifru kako bi napravili nalog.</p><div class="form-group"><input type="password" class="form-control" name="password" placeholder="Password"></div>';
              $createAcount += '<input type="hidden" name="__anti-forgery-token2" val="' + $form.find('input[name="__anti-forgery-token"]').val() + '"/>';
              $createAcount += '<button type="submit" class="btn btn-dark button-send">Napravi nalog</button></form>';
              $createAcount = $($createAcount);

              $form.find('button[type="submit"]').webuiPopover({trigger:'manual', cache: false, title: "Uspešno poručeno.", closeable: true, content: $createAcount});
              $form.find('button[type="submit"]').webuiPopover('show');
              /*$createAcount.find('form').on('submit',function(e){
                  e.preventDefault();
                  var formData = $(this).serialize();
                  alert('pre slanja');
                  $.ajax({
                              type: "POST",
                              url: '/dost/register/after-order',
                              data: formData,
                              dataType: 'json'
                              }).success(function(msg){
                                  $form.find('button[type="submit"]').webuiPopover('hide');
                              })});
                              */

          });
	});

	$('body').on('submit', 'form[data-role="after-order-register"]', function(e){
	  e.preventDefault();
      var formData = $(this).serialize();
      $.ajax({
          type: "POST",
          url: '/dost/register/after-order',
          data: formData,
          dataType: 'json'
      }).success(function(msg){
          $('form[data-role="order-form"]').find('button[type="submit"]').webuiPopover('hide');
      });
	});


    var login = '<form method="POST" data-role="login-popover-form" action="/dost/login"><p>Zaboravili ste sifru?</p><div class="form-group"><input type="email" class="form-control" name="username" placeholder="Email"><input type="password" class="form-control" name="password" placeholder="Password"></div>';
        login += '<button type="submit" class="btn btn-dark button-send">Uloguj se</button></form>';
    $('a[data-role="login-popover-link"]').webuiPopover({trigger:'click', cache: false, title: "Login.", closeable: true, content: login});
    $('body').on('submit', 'form[data-role="login-popover-form"]', function(e){

          var $form = $(this),
              formData = $form.serialize();
          e.preventDefault();
          $.ajax({
              type: "POST",
              url: '/dost/login',
              data: formData,
              dataType: 'json'
           }).success(function(msg){
              setLoggedInUserData(msg['user-data'].user);
              fillFormWithUserData('form[data-role="order-form"]',msg['user-data'].user);
              $('a[data-role="login-popover-link"]').webuiPopover('hide');
              $('a[data-role="login-popover-link"]').toggleClass('hide').siblings('[data-role="loged-in-popover-link"]').toggleClass('hide').text(msg['user-data'].user.email);
           }).fail(function(msg){
           console.log('msg send-conf-email> ' + msg['send-conf-email'])
               if (msg.responseJSON['send-conf-email']){
                   console.log('show link!');
                   if ($form.find('[data-role="resend-conf-email"').size === 0){
                       $form.append('<a href="#" class="" data-role="resend-conf-email">Nalog nije aktiviran. Pošaljite aktivacioni email.</a>');
                   }
               }
               console.log('eror' +JSON.stringify(msg));
           });
     });

    // resend conf email listener
    $('body').on('click', '[data-role="resend-conf-email"]',function(e){
        e.preventDefault();
        var $elem = $(this),
            email = $elem.closest('form').find('input[type="email"]').val();

        $.ajax({
              type: "POST",
              url: '/dost/register/resend-conf-email',
              data: {email: email},
              dataType: 'json'
           }).success(function(resp){
               $elem.replaceWith('<span>Kliknite na aktivacioni link koji ste dobili u poruci.</span>');
           });
    });
    var logedInOptions = '<ul><li class="shopping"><a href="/porudzbine.html">Vaše porudzbine</a></li><li class="shopping"><a href="/dost/logout">Logout</a></li></ul>';
    $('a[data-role="loged-in-popover-link"]').webuiPopover({trigger:'click', cache: false, title: "Login.", closeable: true, content: logedInOptions});

    $('body').on('click', 'a[data-role="fill-form-with-account-data"]', function(e){
        e.preventDefault();
        if (loggedInUserData){
            fillFormWithUserData('form[data-role="order-form"]',loggedInUserData);
        }else{

        }
    });
     // fill loged in user data
     function fillForm(){
         if (loggedInUserData) {
             return '<a href="#" data-role="fill-form-with-account-data" class="btn btn-default btn-small">Popuni formu.</a>';
         }else {
             return '<a href="#" class="btn btn-default" data-role="login-from-order-form">uloguj se</a>';
         }
     }

    $('body').on('click','a[data-role="login-from-order-form"]',function(e){
        e.preventDefault();
        var $login = $('a[data-role="login-popover-link"]')
         $('html, body').animate({
                scrollTop: $login.offset().top
            }, 400, function(){
                $login.webuiPopover('show');
            });
    });

     $('a[data-role="loged-in-user-fill-form"]').webuiPopover({trigger:'hover', cache: false, title: "Popuni formu podacima iz naloga.", closeable: true, content: fillForm});
     $('a[data-role="loged-in-user-fill-form"]').click(function(e){
         e.preventDefault();
         $(this).webuiPopover('show');
     });

}(jQuery))