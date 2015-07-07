package org.ovirt.engine.ui.webadmin.section.main.view.popup.gluster;

import org.ovirt.engine.core.common.businessentities.gluster.BrickProfileDetails;
import org.ovirt.engine.core.common.businessentities.gluster.FopStats;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeProfileStats;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.idhandler.WithElementId;
import org.ovirt.engine.ui.common.view.popup.AbstractModelBoundPopupView;
import org.ovirt.engine.ui.common.widget.dialog.RefreshActionIcon;
import org.ovirt.engine.ui.common.widget.dialog.SimpleDialogPanel;
import org.ovirt.engine.ui.common.widget.dialog.tab.DialogTab;
import org.ovirt.engine.ui.common.widget.editor.EntityModelCellTable;
import org.ovirt.engine.ui.common.widget.editor.ListModelListBoxEditor;
import org.ovirt.engine.ui.common.widget.renderer.NameRenderer;
import org.ovirt.engine.ui.common.widget.table.column.AbstractEntityModelTextColumn;
import org.ovirt.engine.ui.uicommonweb.models.ListModel;
import org.ovirt.engine.ui.uicommonweb.models.gluster.VolumeProfileStatisticsModel;
import org.ovirt.engine.ui.uicompat.Event;
import org.ovirt.engine.ui.uicompat.IEventListener;
import org.ovirt.engine.ui.uicompat.PropertyChangedEventArgs;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.ApplicationResources;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.section.main.presenter.popup.gluster.VolumeProfileStatisticsPopupPresenterWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class VolumeProfileStatisticsPopupView extends AbstractModelBoundPopupView<VolumeProfileStatisticsModel> implements VolumeProfileStatisticsPopupPresenterWidget.ViewDef {

    interface Driver extends SimpleBeanEditorDriver<VolumeProfileStatisticsModel, VolumeProfileStatisticsPopupView> {
    }

    interface ViewUiBinder extends UiBinder<SimpleDialogPanel, VolumeProfileStatisticsPopupView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    interface ViewIdHandler extends ElementIdHandler<VolumeProfileStatisticsPopupView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    @UiField
    DialogTab bricksTab;

    @UiField
    @Ignore
    @WithElementId
    Label bricksErrorLabel;

    @UiField(provided = true)
    @Path(value = "bricks.selectedItem")
    @WithElementId
    ListModelListBoxEditor<BrickProfileDetails> bricks;

    @UiField(provided = true)
    RefreshActionIcon brickRefreshIcon;

    @UiField(provided = true)
    @Ignore
    @WithElementId
    EntityModelCellTable<ListModel> volumeProfileStats;

    @UiField
    @Ignore
    @WithElementId
    Label profileRunTime;

    @UiField
    @Ignore
    @WithElementId
    Label bytesRead;

    @UiField
    @Ignore
    @WithElementId
    Label bytesWritten;

    @UiField
    @WithElementId
    Anchor brickProfileAnchor;

    @UiField
    DialogTab nfsTab;

    @UiField
    @Ignore
    @WithElementId
    Label nfsErrorLabel;

    @UiField(provided = true)
    @Path(value = "nfsServers.selectedItem")
    @WithElementId
    ListModelListBoxEditor<GlusterVolumeProfileStats> nfsServers;

    @UiField(provided = true)
    RefreshActionIcon nfsRefreshIcon;

    @UiField(provided = true)
    @Ignore
    @WithElementId
    EntityModelCellTable<ListModel> nfsServerProfileStats;

    @UiField
    @Ignore
    @WithElementId
    Label nfsProfileRunTime;

    @UiField
    @Ignore
    @WithElementId
    Label nfsBytesRead;

    @UiField
    @Ignore
    @WithElementId
    Label nfsBytesWritten;

    @UiField
    @WithElementId
    Anchor nfsProfileAnchor;

    private final static ApplicationResources resources = AssetProvider.getResources();
    private final static ApplicationConstants constants = AssetProvider.getConstants();

    private final Driver driver = GWT.create(Driver.class);

    @Inject
    public VolumeProfileStatisticsPopupView(EventBus eventBus) {
        super(eventBus);
        initEditors();
        initWidget(ViewUiBinder.uiBinder.createAndBindUi(this));
        ViewIdHandler.idHandler.generateAndSetIds(this);
        localize();
        nfsErrorLabel.setVisible(false);
        bricksErrorLabel.setVisible(false);
        driver.initialize(this);
    }

    private void localize() {
        bricks.setLabel(constants.selectBrickToViewFopStats());
        nfsServers.setLabel(constants.selectServerToViewFopStats());
        bricksTab.setLabel(constants.volumeProfileBricksTab());
        nfsTab.setLabel(constants.volumeProfileNfsTab());
        bricksErrorLabel.setText(constants.brickProfileErrorMessage());
        nfsErrorLabel.setText(constants.nfsProfileErrorMessage());
    }

    private void initEditors() {
        nfsRefreshIcon = new RefreshActionIcon(SafeHtmlUtils.EMPTY_SAFE_HTML);
        brickRefreshIcon = new RefreshActionIcon(SafeHtmlUtils.EMPTY_SAFE_HTML);
        bricks = new ListModelListBoxEditor<>(new NameRenderer<BrickProfileDetails>());
        nfsServers = new ListModelListBoxEditor<>(new NameRenderer<GlusterVolumeProfileStats>());
        volumeProfileStats = new EntityModelCellTable<ListModel>(false, true);

        volumeProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getName();
            }
        }, constants.fileOperation());

        volumeProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getHits() + "";
            }
        }, constants.fOpInvocationCount());

        volumeProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getMaxLatencyFormatted().getFirst() + " " + entity.getMaxLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpMaxLatency());

        volumeProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getMinLatencyFormatted().getFirst() + " " + entity.getMinLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpMinLatency());

        volumeProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getAvgLatencyFormatted().getFirst() + " " + entity.getAvgLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpAvgLatency());

        nfsServerProfileStats = new EntityModelCellTable<ListModel>(false, true);

        nfsServerProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getName();
            }
        }, constants.fileOperation());

        nfsServerProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getHits() + "";
            }
        }, constants.fOpInvocationCount());

        nfsServerProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getMaxLatencyFormatted().getFirst() + " " + entity.getMaxLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpMaxLatency());

        nfsServerProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getMinLatencyFormatted().getFirst() + " " + entity.getMinLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpMinLatency());

        nfsServerProfileStats.addColumn(new AbstractEntityModelTextColumn<FopStats>() {
            @Override
            protected String getText(FopStats entity) {
                return entity.getAvgLatencyFormatted().getFirst() + " " + entity.getAvgLatencyFormatted().getSecond();//$NON-NLS-1$
            }
        }, constants.fOpAvgLatency());

    }

    private void initAnchor(String url, Anchor anchor) {
        anchor.setHref(url);
        anchor.setText(constants.exportToPdf());
        anchor.setTarget("_blank");//$NON-NLS-1$
    }

    @Override
    public void edit(final VolumeProfileStatisticsModel object) {
        driver.edit(object);
        volumeProfileStats.asEditor().edit(object.getCumulativeStatistics());
        nfsServerProfileStats.asEditor().edit(object.getNfsServerProfileStats());
        profileRunTime.setText(object.getProfileRunTime());
        nfsProfileRunTime.setText(object.getNfsProfileRunTime());
        bytesRead.setText(object.getBytesRead());
        nfsBytesRead.setText(object.getNfsBytesRead());
        bytesWritten.setText(object.getBytesWritten());
        nfsBytesWritten.setText(object.getNfsBytesWritten());

        ClickHandler brickTabClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                object.queryBackend(true);
            }
        };

        brickRefreshIcon.setRefreshIconClickListener(brickTabClickHandler);

        ClickHandler nfsTabClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                object.queryBackend(false);
            }
        };

        nfsRefreshIcon.setRefreshIconClickListener(nfsTabClickHandler);

        object.getPropertyChangedEvent().addListener(new IEventListener<PropertyChangedEventArgs>() {
            @Override
            public void eventRaised(Event<? extends PropertyChangedEventArgs> ev, Object sender, PropertyChangedEventArgs args) {
                if (args.propertyName.equals("brickProfileRunTimeChanged")) {//$NON-NLS-1$
                    profileRunTime.setText(object.getProfileRunTime());
                }
                if (args.propertyName.equals("brickProfileDataRead")) {//$NON-NLS-1$
                    bytesRead.setText(object.getBytesRead());
                }
                if (args.propertyName.equals("brickProfileDataWritten")) {//$NON-NLS-1$
                    bytesWritten.setText(object.getBytesWritten());
                }
                if (args.propertyName.equals("nfsProfileRunTimeChanged")) {//$NON-NLS-1$
                    nfsProfileRunTime.setText(object.getNfsProfileRunTime());
                }
                if (args.propertyName.equals("nfsProfileDataRead")) {//$NON-NLS-1$
                    nfsBytesRead.setText(object.getNfsBytesRead());
                }
                if (args.propertyName.equals("nfsProfileDataWritten")) {//$NON-NLS-1$
                    nfsBytesWritten.setText(object.getNfsBytesWritten());
                }
                if(args.propertyName.equals("statusOfFetchingProfileStats")) {//$NON-NLS-1$
                    boolean disableErrorLabels = !(object.isSuccessfulProfileStatsFetch());
                    if(!disableErrorLabels) {
                        String url = object.getProfileExportUrl();
                        boolean isBrickTabSelected = !(url.contains(";nfsStatistics=true"));//$NON-NLS-1$
                        initAnchor(url, (isBrickTabSelected) ? brickProfileAnchor : nfsProfileAnchor);
                    }
                    bricksErrorLabel.setVisible(disableErrorLabels);
                    nfsErrorLabel.setVisible(disableErrorLabels);
                }
            }
        });
    }

    @Override
    public VolumeProfileStatisticsModel flush() {
        return driver.flush();
    }
}
