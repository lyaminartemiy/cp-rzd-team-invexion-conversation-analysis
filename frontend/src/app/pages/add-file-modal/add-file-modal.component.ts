import {Component, signal} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {AnalyzeService} from "../../core/services/analyze.service";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-add-file-modal',
  templateUrl: './add-file-modal.component.html',
  styleUrls: ['./add-file-modal.component.scss']
})
export class AddFileModalComponent {
  constructor(public dialogRef: MatDialogRef<AddFileModalComponent>, private analyzeService:AnalyzeService, private notifierService:NotifierService,) {
  }
  file: File | null
  errorWithExtension = false;
  loading2 = signal(false);

  getFile(event:any) {
    if (event.target.files.length > 0) {
      event.preventDefault();
      this.file = event.target.files[0];
      if ((this.file?.name.match(/\.([^.]+)$/)?.[1] === 'zip' || this.file?.name.match(/\.([^.]+)$/)?.[1] === 'wav' || this.file?.name.match(/\.([^.]+)$/)?.[1] === 'mp3') &&  this.file?.size! <= 20 * 1024 * 1024) {
        this.errorWithExtension = false;
      } else {
        this.errorWithExtension = true;
        this.file = null;
      }
    } else {
      this.errorWithExtension = true;
      this.file = null;
    }
  }

  uploadFile() {
    console.log(this.file?.name)
    this.loading2.set(true);
    if(this.file?.name.match(/\.([^.]+)$/)?.[1] === 'zip')  this.analyzeService.uploadZipFile(this.file).subscribe({
      next: (res) => {
        this.loading2.set(false)
        this.errorWithExtension = false;
        this.file = null;
        this.notifierService.notify('success', 'Файл успешно добавлен!');
        // this.scroll(this.reftp)
      },
      error: () => {
      },
      complete: () => {
      }
    });
    else if(this.file?.name.match(/\.([^.]+)$/)?.[1] === 'wav' || this.file?.name.match(/\.([^.]+)$/)?.[1] === 'mp3')  this.analyzeService.uploadOtherFile(this.file).subscribe({
      next: (res) => {
        this.loading2.set(false)
        this.errorWithExtension = false;
        this.file = null;
        this.notifierService.notify('success', 'Файл успешно добавлен!');
        this.dialogRef.close()
        // this.scroll(this.reftp)
      },
      error: () => {
      },
      complete: () => {
      }
    });

  }
  closeDialog() {
    this.dialogRef.close()
  }

  cancel() {
    this.errorWithExtension = false;
    this.file = null;
  }
}
