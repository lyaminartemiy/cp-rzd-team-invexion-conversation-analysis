import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricModelComponent } from './metric-model.component';

describe('MetricModelComponent', () => {
  let component: MetricModelComponent;
  let fixture: ComponentFixture<MetricModelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MetricModelComponent]
    });
    fixture = TestBed.createComponent(MetricModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
