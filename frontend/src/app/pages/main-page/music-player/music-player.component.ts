import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {It} from "../../metric-model/metric-model.component";
import {AnalyzeService} from "../../../core/services/analyze.service";
import {MetricsCalculate, TextIntervals} from "../../../core/models/analyze";
export interface Its {
  id: string,
  text: string,
  analysisId: number
}
@Component({
  selector: 'app-music-player',
  templateUrl: './music-player.component.html',
  styleUrls: ['./music-player.component.scss']
})
export class MusicPlayerComponent implements OnInit{
constructor(public dialogRef: MatDialogRef<MusicPlayerComponent>, @Inject(MAT_DIALOG_DATA) public data: Its, private analyzeService:AnalyzeService) {
}
metrics: TextIntervals[] = [];
  lastEndIndex: number = 0;

  closeDialog() {
    console.log(this.data.text)
    this.dialogRef.close()
  }

  ngOnInit(): void {
    this.analyzeService.getViolationsByAnalysisId(this.data.analysisId).subscribe((res) => {
      if (res) this.metrics = res
    })
  }
}
