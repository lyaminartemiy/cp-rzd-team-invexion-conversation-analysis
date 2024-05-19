import {AfterViewInit, ChangeDetectorRef, Component, ContentChild, OnInit, signal, ViewChild} from '@angular/core';
import {AggregatedMetrics, AllAnalyst, MetricsCalculate, TextIntervals} from "../../core/models/analyze";
import {AnalyzeService} from "../../core/services/analyze.service";
import {tap} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {NotifierService} from "angular-notifier";
import {MatSelectChange} from "@angular/material/select";
import {FormControl} from "@angular/forms";
import {MetricModelComponent} from "../metric-model/metric-model.component";
import {MatDialog} from "@angular/material/dialog";
import {AddFileModalComponent} from "../add-file-modal/add-file-modal.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {
  constructor(public dialog: MatDialog,private analyzeService:AnalyzeService, private notifierService:NotifierService, private detectorRef:ChangeDetectorRef, private router: Router) {
  }
  expandedElement: AllAnalyst | null;

  allMetrics: AggregatedMetrics[];
  negotiations: AllAnalyst[] = [];
  filteredNegotiations: AllAnalyst[] = [];
  dataSource:AllAnalyst[];
  size = 5;
  pageIndex = 0;
  intervals: TextIntervals[] = [
    {
      "id": 0,
      "violatedText": "Нарушенный текст 1",
      "beginIndex": 10,
      "endIndex": 20,
      "violatedRegulation": "Правило 1"
    },
    {
      "id": 1,
      "violatedText": "Нарушенный текст 2",
      "beginIndex": 30,
      "endIndex": 40,
      "violatedRegulation": "Правило 2"
    },
    {
      "id": 2,
      "violatedText": "Нарушенный текст 3",
      "beginIndex": 50,
      "endIndex": 60,
      "violatedRegulation": "Правило 3"
    }
  ]  
  lastEndIndex: number = 0;

    text: string = 'Этот пример содержит некоторый текст, в котором можно найти различные нарушения и ошибки. Например, вот фраза Нарушенный текст 1, которая может содержать опечатку или несоответствие правилам. Далее идет другой кусок текста, в котором встречается \'Нарушенный текст 2\' - здесь также можно обнаружить несоответствие правилам. И, наконец, последний пример: Нарушенный текст 3, который требует правки. Этот пример содержит некоторый текст, в котором можно найти различные нарушения и ошибки. Например, вот фраза \'Нарушенный текст 1\', которая может содержать опечатку или несоответствие правилам. Далее идет другой кусок текста, в котором встречается \'Нарушенный текст 2\' - здесь также можно обнаружить несоответствие правилам. И, наконец, последний пример: ' +
      'Нарушенный текст, который требует правки. Этот пример содержит некоторый текст, в котором можно найти различные нарушения и ошибки. Например, вот фраза Нарушенный текст 1\', которая может содержать опечатку или несоответствие правилам. Далее идет другой кусок текста, в котором встречается \'Нарушенный текст 2\' - здесь также можно обнаружить несоответствие правилам. И, наконец, последний пример:' +
      ' Нарушенный текст 3\', который требует правки. Этот пример содержит некоторый текст, в котором можно найти различные нарушения и ошибки. Например, вот фраза \'Нарушенный текст 1\', которая может содержать опечатку или несоответствие правилам. Далее идет другой кусок текста, в котором встречается \'Нарушенный текст 2\' - здесь также можно обнаружить несоответствие правилам. И, наконец, последний пример: \'Нарушенный текст 3\', который требует правки'
  ngOnInit(): void {
    this.getNegotiations();
    this.filterInput.valueChanges.subscribe((res)=>{
      this.filteredNegotiations = this.negotiations.filter(r => r.fileName.includes(res));
      this.size = 5;
      this.paginate(1, this.filteredNegotiations)
    })
  }
  loading = signal(false);
  loading2 = signal(false);
  loading3 = signal(false);

  scroll(el: HTMLElement) {
    console.log(el)
      el.scrollIntoView({ behavior: "smooth" });
      console.log("Scrolling to " + el.getAttribute("id"))
  }
  getAllMetrics() {
    this.analyzeService.getAllMetrics().subscribe({
      next: (res) => {
       if(res) this.allMetrics = res.aggregatedMetrics.filter(r => r.value>=0);
      },
      error: () => {
      },
      complete: () => {
      }
    });
  }
  file: File | null
  errorWithExtension = false;
  displayedColumns: string[] = ['Выбрать', 'Файл', 'Тип файла', 'Дата загрузки', 'Проанализировано', 'Действие'];
  analytics: MetricsCalculate[] = [];
  reftp = document.getElementById("first");
  filterInput: FormControl = new FormControl('');
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
       this.getNegotiations();
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
        this.getNegotiations();
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

  }

  cancel() {
    this.errorWithExtension = false;
    this.file = null;
  }
  getNegotiations() {
    this.loading.set(true);
    this.analyzeService.getNegotiations().pipe(tap(qw => this.loading.set(false))).subscribe({
      next: (res) => {
        this.filterInput.reset();
        this.negotiations = res ? res : [];
        this.negotiations.forEach(r => r.isSelected = false);
        this.filteredNegotiations =  this.negotiations;
        this.dataSource = this.negotiations
      },
      error: () => {
      },
      complete: () => {
      }
    })
  }

  changeValue(event: MatCheckboxChange, element: AllAnalyst) {
    if(this.negotiations) {
      this.negotiations.find(r => r?.id === element?.id)!.isSelected = event.checked;
    }
    console.log(event.checked, element);
  }
  checkActive() {
    return this.negotiations.some(i => i.isSelected==true)
  }

  changeValue1(event: MatCheckboxChange) {
    if(event.checked) {
      this.negotiations.forEach(r => r.isSelected = true)
    }
    else this.negotiations.forEach(r => r.isSelected = false)
  }
  paginate(event: any, data: AllAnalyst[]) {
    this.pageIndex=event;
    this.dataSource = data.slice(event * this.size - this.size, event * this.size);
  }

  checkEvent(event: MatSelectChange) {
    this.size = event.value;
    this.paginate(1, this.filteredNegotiations)
  }

  checkOne(id: number) {
    let string = id.toString();
    this.router.navigate(['analyze'],  { state: { ids: string} })
    // this.dialog.open(MetricModelComponent, {
    //     width      : '100%',
    //   height     : '100%',
    //   autoFocus: false,
    //     hasBackdrop: true,
    //     disableClose: true,
    //   data : {
    //       ids: string
    //   },
    // })
  }

  goToModel() {
    let string1:string = '';
    this.negotiations.forEach(it => {
      if(it.isSelected) string1+=it.id.toString();
      string1+=','
    });
    this.dialog.open(MetricModelComponent, {
      width      : '100%',
      height     : '90%',
      autoFocus: false,
      hasBackdrop: true,
      disableClose: true,
      data : {
        ids: string1.slice(0, -1)
      },
    })
  }
}
