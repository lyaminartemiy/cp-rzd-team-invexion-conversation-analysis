import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllMetricsComponent } from './all-metrics.component';

describe('AllMetricsComponent', () => {
  let component: AllMetricsComponent;
  let fixture: ComponentFixture<AllMetricsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllMetricsComponent]
    });
    fixture = TestBed.createComponent(AllMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
