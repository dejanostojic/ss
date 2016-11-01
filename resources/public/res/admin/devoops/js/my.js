
 // dialogs
            $('body').on('click', '.ja-product-delete-modal', function(e) {
                e.preventDefault();
                e.stopPropagation();

                $this = $(this);
                // show dialog
                BootstrapDialog.show({
                    title: 'Confirm Delete product!',
                    type: BootstrapDialog.TYPE_DANGER,
                    data: {
                        method: 'DELETE',
                        action: $this.attr('data-action'),
                        $clickedRow: $this.closest('tr'),
                        name: $this.attr('data-name')
                    },
                    message: 'Delete ' + $this.attr('data-name') + '?',
                    buttons: [{
                        label: 'Get title drop value.',
                        action: function (dialog) {
                            //alert(dialogItself.getData('field-title-drop').val());
                            console.log('action: ' + dialog.getData('action'));
                            console.log('method: ' + dialog.getData('method'));
                            dialog.close();

                        }
                    },
                    {
                        label: 'Delete',
                        action: function (dialog) {
                            //alert(dialogItself.getData('field-title-drop').val());
                            console.log('action: ' + dialog.getData('action'));
                            console.log('method: ' + dialog.getData('method'));
                            $.ajax({
                                type: dialog.getData('method'),
                                url: dialog.getData('action'),
                                cache: false,
                                dataType: 'json',
                                success: function( data, textStatus, jqXHR ){
                                    console.log('data: ' + data);
                                    console.log('textStatus: ' + textStatus);
                                    console.log('jqXHR: ' + JSON.stringify(jqXHR));

                                    if (jqXHR.status == 204){
                                        dialog.getData('$clickedRow').remove();
                                        dialog.close();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    alert('error');
                                    console.log('textStatus: ' + textStatus);
                                    console.log('jqXHR: ' + JSON.stringify(jqXHR));
                                }
                            });

                        }
                    }]
                });
            });