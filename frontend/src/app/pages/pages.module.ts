import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatCardModule} from "@angular/material/card";
import {RouterModule} from "@angular/router";
import {MatDialogModule} from "@angular/material/dialog";
import {MatDividerModule} from "@angular/material/divider";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSelectModule} from "@angular/material/select";
import {MatNativeDateModule, MatOptionModule} from "@angular/material/core";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {NotifierModule, NotifierOptions} from "angular-notifier";
import {LineChartModule, PieChartModule} from "@swimlane/ngx-charts";
import {MatTableModule} from "@angular/material/table";
import {CdkColumnDef, CdkTableModule} from "@angular/cdk/table";
import {MatDatepickerModule} from "@angular/material/datepicker";
import { MainPageComponent } from './main-page/main-page.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {NgxPaginationModule} from "ngx-pagination";
import { MetricModelComponent } from './metric-model/metric-model.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatMenuModule} from "@angular/material/menu";
import {HeaderComponent} from "./header/header.component";
import {MatExpansionModule} from "@angular/material/expansion";
import { AddFileModalComponent } from './add-file-modal/add-file-modal.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { MusicPlayerComponent } from './main-page/music-player/music-player.component';
import { MainComponent } from './main/main.component';
import { AllMetricsComponent } from './all-metrics/all-metrics.component';
const customNotifierOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'right',
      distance: 12
    },
    vertical: {
      position: 'top',
      distance: 12,
      gap: 10
    }
  },
  theme: 'material',
  behaviour: {
    autoHide: 5000,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};
@NgModule({
    declarations: [
        MainPageComponent,
        MetricModelComponent,
        HeaderComponent,
        AddFileModalComponent,
        MusicPlayerComponent,
        MainComponent,
        AllMetricsComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatProgressBarModule,
        MatCardModule,
        RouterModule,
        MatNativeDateModule,
        MatDialogModule,
        MatIconModule,
        MatOptionModule,
        MatSelectModule,
        MatCheckboxModule,
        MatButtonToggleModule,
        NotifierModule.withConfig(customNotifierOptions),
        PieChartModule,
        MatTableModule,
        CdkTableModule,
        LineChartModule,
        MatDatepickerModule,
        MatProgressSpinnerModule,
        MatPaginatorModule,
        NgxPaginationModule,
        MatTooltipModule,
        MatMenuModule,
        MatExpansionModule,
    ],
  providers:[CdkColumnDef]

})
export class PagesModule { }
