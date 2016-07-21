jQuery(document).ready(function($){
    // Mobile Button
    var touch = $('#mobile-button');
    var menu = $('nav#main-menu');
        $(touch).on('click', function (e) {
        e.preventDefault();
        menu.slideToggle();
        });
        $(window).resize(function () {
        var w = $(window).width();
        if (w > 767 && menu.is(':hidden')) {
            menu.removeAttr('style');
        }
    });

    // Connect Owl-Карусель
    var owl = $("#header-slider");
    owl.owlCarousel({
    itemsCustom : [[0, 1], [400, 1], [700, 1], [1000, 1], [1200, 1], [1600, 1]],
    autoPlay : 20000,
    slideSpeed : 200,
    pagination : true
    });

    // Custom Navigation Events
    $(".next").click(function(){
      owl.trigger('owl.next');
    });
    $(".prev").click(function(){
      owl.trigger('owl.prev');
    });
    // One height on action blocks
    $(function() {
        // apply your matchHeight on DOM ready (they will be automatically re-applied on load or resize)
        var byRow = $('body').hasClass('test-rows');
        // apply matchHeight to each item container's items
        $('.one-height').each(function() {
            $(this).children('.col-sm-6').matchHeight({
                byRow: byRow
            });
        });
    });
    // Bootstrap toggle icon collapse
    $('a[data-toggle="collapse"]').click(function () {
        $(this).find('span.toggle-icon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
    })

    // Find and append open menu button
     $(".toggle-navigation li").append( function(indx, val){
       if($('<ul>'+val+'</ul>').find(".sub-menu").length != 0)
         return '<span class="chevron-down-button"><i class="fa fa-chevron-down"></i></span>';
       else
         return "";
    });
    // Toggle menu button
    $("ul.toggle-navigation > li > span.chevron-down-button").click(function () {
        console.log(this)
        $(this).parent().children().toggle();
        $(this).toggle().toggleClass('up');
    }).click();

    // Styling select
    $('select').styler();
});
// Other script's
jQuery(document).ready(function($){
'use strict';

;( function ( document, window, index )
{
    var inputs = document.querySelectorAll( '.inputfile' );
    Array.prototype.forEach.call( inputs, function( input )
    {
        var label    = input.nextElementSibling,
            labelVal = label.innerHTML;

        input.addEventListener( 'change', function( e )
        {
            var fileName = '';
            if( this.files && this.files.length > 1 )
                fileName = ( this.getAttribute( 'data-multiple-caption' ) || '' ).replace( '{count}', this.files.length );
            else
                fileName = e.target.value.split( '\\' ).pop();

            if( fileName )
                label.querySelector( 'span' ).innerHTML = fileName;
            else
                label.innerHTML = labelVal;
        });

        // Firefox bug fix
        input.addEventListener( 'focus', function(){ input.classList.add( 'has-focus' ); });
        input.addEventListener( 'blur', function(){ input.classList.remove( 'has-focus' ); });
    });
}( document, window, 0 ));
});