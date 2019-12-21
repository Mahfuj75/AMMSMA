package asa.org.bd.ammsma.customAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.MemberListInfo;


public class SpinnerCustomAdapter {

    public ArrayAdapter arrayListAdapterForSpinner(final List<String> groupNames, Context context, final List<Integer> defaultProgramIdList, final int spinnerPosition) {

        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_item_for_group, groupNames) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textViewSpinner = view.findViewById(R.id.textViewSpinner);
                if (position % 2 == 0) {
                    if (spinnerPosition == position) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundResource(R.drawable.spinner_ripple_group_selected);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#B2DFDB"));
                        }

                    } else {

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundResource(R.drawable.spinner_ripple_group_even);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        }

                    }


                    if (defaultProgramIdList.get(position) == 999) {
                        textViewSpinner.setTextColor(Color.parseColor("#DD2C00"));
                    } else if (groupNames.get(position).contains("*")) {
                        textViewSpinner.setTextColor(Color.parseColor("#1A237E"));
                    } else {
                        textViewSpinner.setTextColor(Color.parseColor("#212121"));
                    }
                } else {

                    if (spinnerPosition == position) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundResource(R.drawable.spinner_ripple_group_selected);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#B2DFDB"));
                        }
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            view.setBackgroundResource(R.drawable.spinner_ripple_group_odd);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                    }

                    if (defaultProgramIdList.get(position) == 999) {
                        textViewSpinner.setTextColor(Color.parseColor("#DD2C00"));
                    } else if (groupNames.get(position).contains("*")) {
                        textViewSpinner.setTextColor(Color.parseColor("#1A237E"));
                    } else {
                        textViewSpinner.setTextColor(Color.parseColor("#212121"));
                    }
                }
                return view;

            }

        };
    }

    public ArrayAdapter arrayListAdapterForRealizedINfoGroup(final List<String> groupNames, Context context, final List<Integer> defaultProgramIdList) {

        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_item_for_realized_group, groupNames) {


            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                final CheckedTextView checkedTextView = view.findViewById(R.id.checkTextViewSpinner);
                if (position % 2 == 0) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_group_even);
                    } else {
                        view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    }

                    if (defaultProgramIdList.get(position) == 999) {
                        checkedTextView.setTextColor(Color.parseColor("#DD2C00"));
                    } else if (groupNames.get(position).contains(" * ")) {
                        checkedTextView.setTextColor(Color.parseColor("#1A237E"));
                    } else {
                        checkedTextView.setTextColor(Color.parseColor("#000000"));
                    }
                } else {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_group_odd);
                    } else {
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }

                    if (defaultProgramIdList.get(position) == 999) {
                        checkedTextView.setTextColor(Color.parseColor("#DD2C00"));
                    } else if (groupNames.get(position).contains(" * ")) {
                        checkedTextView.setTextColor(Color.parseColor("#1A237E"));
                    } else {
                        checkedTextView.setTextColor(Color.parseColor("#000000"));
                    }
                }

                return view;
            }

        };
    }


    public ArrayAdapter arrayListAdapterForRealizedDate(final List<String> dates, Context context) {

        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_item_for_realized_date, dates) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position % 2 == 0) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_group_even);
                    } else {
                        view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    }
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_group_odd);
                    } else {
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
                return view;

            }

        };
    }

    public ArrayAdapter arrayAdapterForMemberListLTS(final List<SpannableStringBuilder> groupNames, Context context) {

        return new ArrayAdapter<SpannableStringBuilder>(context.getApplicationContext(),
                R.layout.spinner_others, groupNames) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    view.setBackgroundResource(R.drawable.spinner_ripple_group_cbs_lts_savings);
                }
                return view;
            }
        };
    }


    public ArrayAdapter arrayAdapterForMemberListDailyCollection(Context context, final List<String> groupNames, final MemberListInfo memberListInfo) {


        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_daily_collection_dynamic, groupNames) {


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (memberListInfo.getMembersPaidOrNot().get(position)) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_green);
                    } else {
                        view.setBackgroundResource(R.drawable.spinner_ripple_white);
                    }
                } else {
                    if (memberListInfo.getMembersPaidOrNot().get(position)) {
                        view.setBackgroundColor(Color.parseColor("#00C853"));
                    } else {
                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }


                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (memberListInfo.getMembersPaidOrNot().get(position)) {
                        view.setBackgroundResource(R.drawable.spinner_ripple_green);
                    } else {
                        view.setBackgroundResource(R.drawable.spinner_ripple_white);
                    }
                } else {
                    if (memberListInfo.getMembersPaidOrNot().get(position)) {
                        view.setBackgroundColor(Color.parseColor("#00C853"));
                    } else {
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
                return view;
            }
        };
    }


    public ArrayAdapter arrayListAdapterForSpinnerMemberBalance(final List<String> groupNames, Context context) {

        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_others, groupNames) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    view.setBackgroundResource(R.drawable.spinner_ripple_group_cbs_lts_savings);
                } /*else{
                    view.setBackgroundResource(R.drawable.round_corner_for_lts_member);
                }*/
                TextView textViewSpinner = view.findViewById(R.id.textViewSpinner);
                if (groupNames.get(position).contains("*")) {
                    textViewSpinner.setTextColor(Color.parseColor("#1A237E"));
                } else {
                    textViewSpinner.setTextColor(Color.parseColor("#000000"));
                }
                return view;

            }

        };
    }


    public ArrayAdapter arrayListAdapterForSpinnerLoan(final List<String> groupNames, Context context) {

        return new ArrayAdapter<String>(context.getApplicationContext(),
                R.layout.spinner_loan_item, groupNames) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textViewSpinner = view.findViewById(R.id.textViewSpinner);
                if (groupNames.get(position).contains("*")) {
                    textViewSpinner.setTextColor(Color.parseColor("#1A237E"));
                } else {
                    textViewSpinner.setTextColor(Color.parseColor("#000000"));
                }
                return view;

            }

        };
    }

    public ArrayAdapter arrayAdapterForMemberListMembersLoan(Context context, final List<SpannableStringBuilder> membersName/*, final List<Integer> hasLoanList*/) {


        return new ArrayAdapter<SpannableStringBuilder>(context.getApplicationContext(),
                R.layout.spinner_loan_item, membersName) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    view.setBackgroundResource(R.drawable.spinner_ripple_group_cbs_lts_savings);
                }
                return view;
            }
        };
    }


}
