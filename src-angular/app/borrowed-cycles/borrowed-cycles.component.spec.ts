import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BorrowedCyclesComponent } from './borrowed-cycles.component';

describe('BorrowedCyclesComponent', () => {
  let component: BorrowedCyclesComponent;
  let fixture: ComponentFixture<BorrowedCyclesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BorrowedCyclesComponent]
    });
    fixture = TestBed.createComponent(BorrowedCyclesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
