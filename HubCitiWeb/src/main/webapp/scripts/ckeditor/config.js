/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
};

CKEDITOR.on('instanceReady', function (evt) {
    //editor
    var editor = evt.editor;

    //webkit not redraw iframe correctly when editor's width is < 310px (300px iframe + 10px paddings)
    if (CKEDITOR.env.webkit && parseInt(editor.config.width) < 310) {
    	if(editor.name == 'retPageShortDescription' || editor.name == 'rtlrshrtDsc' || editor.name == 'spclOffrlngDesc' || editor.name == 'spclOffrshrtDesc')
    	{
		   	var iframe = document.getElementById('cke_contents_' + editor.name).getElementsByTagName('iframe')[0];        
		    iframe.style.display = 'none';
		    iframe.style.display = 'block';
    	}
    }
});