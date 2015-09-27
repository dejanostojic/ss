var Dost = Dost || {};

Dost.history = (function ($,Modernizr) {
	
	/*
	 * Simple plugin for managing history on ajax requests. After every request url has corect value, providing bookmarkable urls for aplication.
	 * If there are no support for HTML5 history API (Uses Modernizr to check support) than simply do not prevent default value of 
	 * 
	 * example of anchor with required attributes
	 * <a href="/admin/page/316/pocetna" data-ajax-url="/admin/page-ajax/316/pocetna" data-title="Sitnoseckana - Pages - Početna" data-target="#ajax-content" class="ajax-link active-parent active">Početna</a>
	 state = {
			href : elem.attr(extOpts.href), // url to set in document location 
			ajaxUrl : elem.attr(extOpts.ajaxUrl), // url to retrieve via XMLHttpRequest
			target : elem.attr(extOpts.targetElem), // target element where to load the content
			title : elem.attr(extOpts.title)  // value for title 
		};
					
	 */
	
	var states = {}, // states object stores states that JavaScript uses to recreate previous page state, without full request. 
		options = {	//	default attribute names
			href: 'href',
			ajaxUrl: 'data-ajax-url',
			title: 'data-page-title',
			targetElem: 'data-target',
			preloader: '.preloader'
		};
	
	function loadAjaxContent(state){
		$('.preloader').toggle("slide");
		
		setTimeout(function(){ // timeout to simulate waiting in local.
			console.log('timeout over!');
			$.ajax({
				mimeType: 'text/html; charset=utf-8', // ! Need set mimeType only when run from local file
				url: state.ajaxUrl,
				type: 'GET',
				success: function(data) {
					tinymce.execCommand('mceRemoveEditor', false, 'wysiwig_full');
					$(state.target).html(data);
					tinymce.execCommand('mceAddEditor', false, 'wysiwig_full');
					$('.preloader').toggle("slide");
					state.callback($('a[href="'+String(state.href)+'"]'));
				},
				error: function (jqXHR, textStatus, errorThrown) {
					console.log(errorThrown);
				},
				dataType: "html",
				async: false
			});
		}, 1000);
		
	}

	return {
		init: function(){
			if (Modernizr.history){
				$(window).on("popstate", function(e) {
					$(window).trigger("dost.history.pathchange"); // handle undo,redo and click in the same handler
//					console.log('popstete: ' + JSON.stringify(e.originalEvent.state))
				});

				$(window).on("dost.history.pathchange", function(e) {
					//console.log(JSON.stringify(e.originalEvent.state));
					var href = window.location.pathname,
					state = states[href];

					if (state){
						if (state._ajaxFn){
							console.log('calls provided function!!');
							state._ajaxFn(state);
						}else{
							loadAjaxContent(state);
						}
					}else{
						window.location = href;
					}
				});
			}
		},
		attach: function(jQueryElems,opts,fn){
			var extOpts = $.extend(options,opts || {});
			
			jQueryElems.on('click',function(e){

				if (Modernizr.history){
					e.preventDefault();
					e.stopPropagation();
					var $elem = $(this);
					var state = {
							href : $elem.attr(extOpts.href),
							ajaxUrl : $elem.attr(extOpts.ajaxUrl),
							target : $elem.attr(extOpts.targetElem),
							title : $elem.attr(extOpts.title)
						};
					
			    	history.pushState(state, state.title , state.href);
			    	state.callback = fn;
			    	state.$elem = $elem;
			    	state.$target= $(state.target);
					states[String(state.href)] = state;
					$(window).trigger("dost.history.pathchange");
					
				}
//				else{
//					document.location = href;
//				}
			});
		},
		attach2: function(params){
			
			var extOpts = $.extend(options,params.opts || {});

			$(params.jQuerySelector).on('click',function(e){

				if (Modernizr.history){
					e.preventDefault();
					var $sourceElem = $(this);
					var state = {
							href : $sourceElem.attr(extOpts.href),
							ajaxUrl : $sourceElem.attr(extOpts.ajaxUrl),
							target : $sourceElem.attr(extOpts.targetElem),
							title : $sourceElem.attr(extOpts.title)
					};
					
					// history.pushState(state, state.title , state.href); // not using this. Cannot add functions or dom elements in state. Saves in memory instead! 
					history.pushState(null, null, state.href);
					
					state._ajaxFn = params.ajaxFn;
					state._beforeAjaxFn = params.beforeAjaxFn;
					state.$sourceElem = $sourceElem;
					state.$target= $(state.target);
					states[String(state.href)] = state;
					$(window).trigger("dost.history.pathchange");
				}
//				else{
//					document.location = href;
//				}
			});
		}	
	};
	
})(jQuery,Modernizr);


/*
 * 
 * 
 * ADDY OSMANY javascript design patterns!!!! Module pattern
 * 
 * 
 * 
var basketModule = (function () {
	 
	  // privates
	 
	  var basket = [];
	 
	  function doSomethingPrivate() {
	    //...
	  }
	 
	  function doSomethingElsePrivate() {
	    //...
	  }
	 
	  // Return an object exposed to the public
	  return {
	 
	    // Add items to our basket
	    addItem: function( values ) {
	      basket.push(values);
	    },
	 
	    // Get the count of items in the basket
	    getItemCount: function () {
	      return basket.length;
	    },
	 
	    // Public alias to a  private function
	    doSomething: doSomethingPrivate,
	 
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

//basketModule returns an object with a public API we can use

basketModule.addItem({
  item: "bread",
  price: 0.5
});
 
basketModule.addItem({
  item: "butter",
  price: 0.3
});
 
// Outputs: 2
console.log( basketModule.getItemCount() );
 
// Outputs: 0.8
console.log( basketModule.getTotal() );
 
// However, the following will not work:
 
// Outputs: undefined
// This is because the basket itself is not exposed as a part of our
// the public API
console.log( basketModule.basket );
 
// This also won't work as it only exists within the scope of our
// basketModule closure, but not the returned public object
console.log( basket );
*/