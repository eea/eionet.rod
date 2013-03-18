-- Add COVERAGE_NOTE column to T_DELIVERY
alter table T_DELIVERY add column COVERAGE_NOTE text default null after COVERAGE;