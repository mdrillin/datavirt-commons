var DATAVIRT_HEADER_DATA_DEFAULTS = {
  "primaryBrand" : "JBoss Data Virtualization",
  "secondaryBrand" : "",
  "username" : "jdoe",
  "logoutLink" : "?GLO=true",
};

var DATAVIRT_HEADER_TEMPLATE = '\
  <div class="datavirt-navbar">\
    <div class="datavirt-navbar-brand">\
      <a class="brand"></a>\
      <a class="subbrand datavirt-desktop-only"></a>\
      <div class="datavirt-nav-user datavirt-desktop-only">\
        <a href="#">\
          <span class="datavirt-nav-username datavirt-header-username"></span>\
        </a>\
      </div>\
      <div class="datavirt-nav-user-menu">\
        <ul>\
          <li><a class="datavirt-nav-logout">Logout</a></li>\
        </ul>\
      </div>\
      <div class="datavirt-nav-mobile datavirt-mobile-only">\
        Menu\
      </div>\
    </div>\
  </div>\
  <div class="datavirt-mobile-nav">\
    <ul class="datavirt-nav-list datavirt-mobile-only">\
      <li class="datavirt-nav-header datavirt-mobile-navigation">Navigation</li>\
      <li class="datavirt-nav-header datavirt-header-username"></li>\
      <li>\
        <a class="datavirt-nav-logout">Logout</a>\
      </li>\
    </ul>\
  </div>';

/**
 * Creates the markup needed for a link in the mobile section of the
 * header.
 * @param tab
 * @returns {String}
 */
function ovl_createMobileLinkHtml(tab) {
    var markup = $('<li><a></a></li>');
    if (tab.active) {
        $(markup).addClass('active');
    }
    $(markup).find('a').attr('href', tab.href);
    $(markup).find('a').text(tab.label);
    return markup;
}

/**
 * Creates the markup needed for a tab in the desktop only navigation
 * section of the header.
 * @param tab
 * @param index
 * @param numTabs
 */
function ovl_createNavigationTab(tab) {
    var markup = $('<a class="datavirt-navbar-tab"></a>');
    if (tab.active) {
        $(markup).addClass('active');
    }
    $(markup).attr('href', tab.href);
    $(markup).text(tab.label);
    return markup;
}

/**
 * Register a function that will render the header when the page loads.  This
 * function expects to find a div with id='datavirt-header', which it will use
 * as the container for the header.  If such a div is not present, the header
 * will not be created.
 */
$(document).ready(function() {
    var data = DATAVIRT_HEADER_DATA_DEFAULTS;
    try {
        data = DATAVIRT_HEADER_DATA;
    } catch (e) {
        // drop
    }
    $('#datavirt-header').html(DATAVIRT_HEADER_TEMPLATE);
    $('#datavirt-header a.brand').text(data.primaryBrand);
    $('#datavirt-header a.subbrand').text(data.secondaryBrand);
    $('#datavirt-header .datavirt-header-username').text(data.username);
    $('#datavirt-header a.datavirt-nav-logout').attr("href", data.logoutLink);
    if (data.tabs) {
        var tabs = data.tabs;
        if (tabs.length > 0) {
            for (var i=0; i < data.tabs.length; i++) {
                var tab = data.tabs[i];
                var tabHtml = ovl_createNavigationTab(tab);
                $('#datavirt-header .datavirt-navbar .datavirt-navbar-tabs').append(tabHtml);
            }
        }
        for (var i = data.tabs.length-1; i >= 0; i--) {
            var tab = data.tabs[i];
            var linkHtml = ovl_createMobileLinkHtml(tab);
            $('#datavirt-header .datavirt-mobile-nav .datavirt-nav-list .datavirt-mobile-navigation').after(linkHtml);
        }
    }
    $('#datavirt-header .datavirt-nav-mobile').click(function() {
        $('#datavirt-header .datavirt-mobile-nav').slideToggle('fast');
    });
    $('#datavirt-header .datavirt-nav-user').click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        var h = $(this).position().top + $(this).outerHeight();
        var w = $(this).outerWidth() - 1;
        $('#datavirt-header .datavirt-nav-user-menu').css({
            'right': 0,
            'top': h,
            'min-width': w
        });
        $('#datavirt-header .datavirt-nav-user-menu').slideToggle('fast');
    });
    $('body').click(function() {
        $('#datavirt-header .datavirt-nav-user-menu').hide();
    });
});

