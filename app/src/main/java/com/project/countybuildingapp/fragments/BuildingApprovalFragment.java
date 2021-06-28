package com.project.countybuildingapp.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.models.Approval;
import com.project.countybuildingapp.models.Certifications;
import com.project.countybuildingapp.models.Comment;
import com.project.countybuildingapp.models.Contractor;
import com.project.countybuildingapp.models.County;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;

import java.io.File;
import java.sql.Timestamp;

public class BuildingApprovalFragment extends Fragment {

    private View view;
    private TextView tvBuildingArchitect, tvBuildingSupervisor, tvBuildingContractor;
    private TextView tvKraCertificateNo, tvNemaCertificateNo, tvSanitationCertificateNo, tvFireSafetyCertificateNo, tvInspectionCertificateNo;
    private TextView tvKraComment, tvNemaComment, tvSanitationComment, tvFireSafetyComment, tvInspectionComment;
    private TextView tvKraContext, tvNemaContext, tvSanitationContext, tvFireSafetyContext, tvInspectionContext;
    private ImageView imgKraDownload, imgNemaDownload, imgSanitationDownload, imgFireSafetyDownload, imgInspectionDownload;
    private Button btnKra, btnNema, btnSanitation, btnFireSafety, btnInspection;
    private ProgressDialog progressDialog;

    private String buildingcode, architectname, supervisorname, contractorname, buildingname, email, countyname, time;
    private String kracertficateno = null, nemacertificateno = null, sanitationcertificateno = null, firesafetycertificateno = null, inspectioncertificateno = null;
    private String kracertficateurl = null, nemacertificateurl = null, sanitationcertificateurl = null, firesafetycertificateurl = null, inspectioncertificateurl = null;
    private int kracontext, nemacontext, sanitationcontext, firesafetycontext, inspectioncontext;

    private BuildingApprovalFragmentArgs args;

    private DatabaseReference contractorreference, certificationreference, countyreference, approvalreference, commentreference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_approval, container, false);

        // set
        ((ToolBarLocker) getActivity()).ToolBarLocked(false);
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        args = BuildingApprovalFragmentArgs.fromBundle(getArguments());
        buildingcode = args.getBuildingCode();
        buildingname = args.getBuildingName();

        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        contractorreference = FirebaseDatabase.getInstance().getReference().child("table_contractor");
        certificationreference = FirebaseDatabase.getInstance().getReference().child("table_certification");
        approvalreference = FirebaseDatabase.getInstance().getReference().child("table_approval");
        countyreference = FirebaseDatabase.getInstance().getReference().child("table_county_registration");
        commentreference = FirebaseDatabase.getInstance().getReference().child("table_comments");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.toString();

        // find view by id
        progressDialog = new ProgressDialog(getContext());

        tvBuildingArchitect = view.findViewById(R.id.tv_buildingarchitect);
        tvBuildingSupervisor = view.findViewById(R.id.tv_buildingsupervisor);
        tvBuildingContractor = view.findViewById(R.id.tv_buildingcontractor);

        tvKraCertificateNo = view.findViewById(R.id.tv_kracertificateno);
        tvNemaCertificateNo = view.findViewById(R.id.tv_nemacertificateno);
        tvSanitationCertificateNo = view.findViewById(R.id.tv_sanitationcertificateno);
        tvFireSafetyCertificateNo = view.findViewById(R.id.tv_firesafetycertificateno);
        tvInspectionCertificateNo = view.findViewById(R.id.tv_inspectioncertificateno);

        tvKraComment = view.findViewById(R.id.tv_kra_comment);
        tvNemaComment = view.findViewById(R.id.tv_nema_comment);
        tvSanitationComment = view.findViewById(R.id.tv_sanitation_comment);
        tvFireSafetyComment = view.findViewById(R.id.tv_firesafety_comment);
        tvInspectionComment = view.findViewById(R.id.tv_inspection_comment);

        tvKraContext = view.findViewById(R.id.tv_kra_context);
        tvNemaContext = view.findViewById(R.id.tv_nema_context);
        tvSanitationContext = view.findViewById(R.id.tv_sanitation_context);
        tvFireSafetyContext = view.findViewById(R.id.tv_firesafety_context);
        tvInspectionContext = view.findViewById(R.id.tv_inspection_context);

        imgKraDownload = view.findViewById(R.id.img_download_kra);
        imgNemaDownload = view.findViewById(R.id.img_download_nema);
        imgSanitationDownload = view.findViewById(R.id.img_download_sanitation);
        imgFireSafetyDownload = view.findViewById(R.id.img_download_firesafety);
        imgInspectionDownload = view.findViewById(R.id.img_download_inspection);

        btnKra = view.findViewById(R.id.btn_kra_approve);
        btnNema = view.findViewById(R.id.btn_nema_approve);
        btnSanitation = view.findViewById(R.id.btn_sanitation_approve);
        btnFireSafety = view.findViewById(R.id.btn_firesafety_approve);
        btnInspection = view.findViewById(R.id.btn_inspection_approve);

        // set / load data
        loadData();

        // listeners
        btnKra.setOnClickListener(kraListener);
        btnNema.setOnClickListener(nemaListener);
        btnSanitation.setOnClickListener(sanitationListener);
        btnFireSafety.setOnClickListener(fireSafetyListener);
        btnInspection.setOnClickListener(inspectionListener);

        imgKraDownload.setOnClickListener(kraDownloadListener);
        imgNemaDownload.setOnClickListener(nemaDownloadListener);
        imgSanitationDownload.setOnClickListener(sanitationDownloadListener);
        imgFireSafetyDownload.setOnClickListener(fireSafetyDownloadListener);
        imgInspectionDownload.setOnClickListener(inspectionDownloadListener);

        tvKraComment.setOnClickListener(kraCommentListener);
        tvNemaComment.setOnClickListener(nemaCommentListener);
        tvSanitationComment.setOnClickListener(sanitationCommentListener);
        tvFireSafetyComment.setOnClickListener(firesafetyCommentListener);
        tvInspectionComment.setOnClickListener(inspectionCommentListener);

        return view;
    }

    // comment
    private View.OnClickListener kraCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateDialog(buildingcode, email, kracertficateno, "kra", kracontext);
        }
    };

    private View.OnClickListener nemaCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateDialog(buildingcode, email, nemacertificateno, "nema", nemacontext);
        }
    };

    private View.OnClickListener sanitationCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateDialog(buildingcode, email, sanitationcertificateno, "sanitation", sanitationcontext);
        }
    };

    private View.OnClickListener firesafetyCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateDialog(buildingcode, email, firesafetycertificateno, "firesafety", firesafetycontext);
        }
    };

    private View.OnClickListener inspectionCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateDialog(buildingcode, email, inspectioncertificateno, "inspection", inspectioncontext);
        }
    };


    //

    private View.OnClickListener kraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (kracertficateno != null) {
                approvalreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_kra").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Do you wish to proceed");
                            builder.setMessage("Approve kra for " + buildingname);
                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_kra").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot != null) {
                                                String key = snapshot.getKey();

                                                Approval approval = new Approval(email, buildingcode, countyname, buildingcode + "_kra", time);
                                                approvalreference.push().setValue(approval).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            certificationreference.child(key).child("status").setValue(1);
                                                            certificationreference.child(key).child("buildingcode_status").setValue(buildingcode + "_1");
                                                            tvKraContext.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error approving", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "Kra details have already been approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "Kra details not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener nemaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (nemacertificateno != null) {
                approvalreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_nema").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Do you wish to proceed");
                            builder.setMessage("Approve nema for " + buildingname);
                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_nema").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot != null) {
                                                String key = snapshot.getKey();

                                                Approval approval = new Approval(email, buildingcode, countyname, buildingcode + "_nema", time);
                                                approvalreference.push().setValue(approval).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            certificationreference.child(key).child("status").setValue(1);
                                                            certificationreference.child(key).child("buildingcode_status").setValue(buildingcode + "_1");
                                                            tvNemaContext.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error approving", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "Nema details have already been approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "Nema details not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener sanitationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (sanitationcertificateno != null) {

                approvalreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_sanitation").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Do you wish to proceed");
                            builder.setMessage("Approve sanitation for " + buildingname);
                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_sanitation").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot != null) {
                                                String key = snapshot.getKey();

                                                Approval approval = new Approval(email, buildingcode, countyname, buildingcode + "_sanitation", time);
                                                approvalreference.push().setValue(approval).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            certificationreference.child(key).child("status").setValue(1);
                                                            certificationreference.child(key).child("buildingcode_status").setValue(buildingcode + "_1");
                                                            tvSanitationContext.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error approving", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "Sanitation details have already been approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "Sanitation details not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private View.OnClickListener fireSafetyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (firesafetycertificateno != null) {
                approvalreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_firesafety").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Do you wish to proceed");
                            builder.setMessage("Approve fire safety for " + buildingname);
                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_firesafety").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot != null) {
                                                String key = snapshot.getKey();

                                                Approval approval = new Approval(email, buildingcode, countyname, buildingcode + "_firesafety", time);
                                                approvalreference.push().setValue(approval).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            certificationreference.child(key).child("status").setValue(1);
                                                            certificationreference.child(key).child("buildingcode_status").setValue(buildingcode + "_1");
                                                            tvFireSafetyContext.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error approving", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "FireSafety details have already been approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "FireSafety details not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private View.OnClickListener inspectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inspectioncertificateno != null) {
                approvalreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_inspection").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Do you wish to proceed");
                            builder.setMessage("Approve inspection for " + buildingname);
                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_inspection").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot != null) {
                                                String key = snapshot.getKey();

                                                Approval approval = new Approval(email, buildingcode, countyname, buildingcode + "_inspection", time);
                                                approvalreference.push().setValue(approval).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            certificationreference.child(key).child("status").setValue(1);
                                                            certificationreference.child(key).child("buildingcode_status").setValue(buildingcode + "_1");
                                                            tvInspectionContext.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Error approving", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "Inspection details have already been approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "Inspection details not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private View.OnClickListener kraDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            download(buildingcode, "kra");
        }
    };

    private View.OnClickListener nemaDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            download(buildingcode, "nema");
        }
    };

    private View.OnClickListener sanitationDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            download(buildingcode, "sanitation");
        }
    };


    private View.OnClickListener fireSafetyDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            download(buildingcode, "firesafety");
        }
    };


    private View.OnClickListener inspectionDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            download(buildingcode, "inspection");
        }
    };

    // load data

    private void loadData() {
        contractorreference.orderByChild("buildingcode").equalTo(buildingcode).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Contractor contractor = snapshot.getValue(Contractor.class);

                    architectname = contractor.getArchitectname();
                    supervisorname = contractor.getSupervisorname();
                    contractorname = contractor.getContractorname();
                }

                tvBuildingArchitect.setText(architectname);
                tvBuildingSupervisor.setText(supervisorname);
                tvBuildingContractor.setText(contractorname);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_kra").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Certifications certifications = snapshot.getValue(Certifications.class);

                    kracertficateno = certifications.getCertificateno();
                    kracertficateurl = certifications.getCertificateurl();
                    kracontext = certifications.getStatus();

                    if (certifications.getCertificateurl() != null) {
                        imgKraDownload.setVisibility(View.VISIBLE);
                    }

                    if (kracontext == 1) {
                        tvKraContext.setVisibility(View.VISIBLE);
                    }
                }

                tvKraCertificateNo.setText(kracertficateno);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_nema").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Certifications certifications = snapshot.getValue(Certifications.class);

                    nemacertificateno = certifications.getCertificateno();
                    nemacertificateurl = certifications.getCertificateurl();
                    nemacontext = certifications.getStatus();

                    if (certifications.getCertificateurl() != null) {
                        imgNemaDownload.setVisibility(View.VISIBLE);
                    }
                }

                tvNemaCertificateNo.setText(nemacertificateno);
                if (nemacontext == 1) {
                    tvNemaContext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_firesafety").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Certifications certifications = snapshot.getValue(Certifications.class);

                    firesafetycertificateno = certifications.getCertificateno();
                    firesafetycertificateurl = certifications.getCertificateurl();
                    firesafetycontext = certifications.getStatus();

                    if (certifications.getCertificateurl() != null) {
                        imgFireSafetyDownload.setVisibility(View.VISIBLE);
                    }
                }

                tvFireSafetyCertificateNo.setText(firesafetycertificateno);
                if (firesafetycontext == 1) {
                    tvFireSafetyContext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_sanitation").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Certifications certifications = snapshot.getValue(Certifications.class);

                    sanitationcertificateno = certifications.getCertificateno();
                    sanitationcertificateurl = certifications.getCertificateurl();
                    sanitationcontext = certifications.getStatus();

                    if (certifications.getCertificateurl() != null) {
                        imgSanitationDownload.setVisibility(View.VISIBLE);
                    }
                }

                tvSanitationCertificateNo.setText(sanitationcertificateno);

                if (sanitationcontext == 1) {
                    tvSanitationContext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_inspection").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getChildrenCount() != 0) {
                    Certifications certifications = snapshot.getValue(Certifications.class);

                    inspectioncertificateno = certifications.getCertificateno();
                    inspectioncertificateurl = certifications.getCertificateurl();
                    inspectioncontext = certifications.getStatus();

                    if (certifications.getCertificateurl() != null) {
                        imgInspectionDownload.setVisibility(View.VISIBLE);
                    }
                }

                tvInspectionCertificateNo.setText(inspectioncertificateno);
                if (inspectioncontext == 1) {
                    tvInspectionContext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        countyreference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    County county = snapshot.getValue(County.class);

                    countyname = county.getCountyname();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CreateDialog(String buildingcodetext, String emailtext, String certificateno, String certificatetype, int status) {
        if (certificateno != null) {

            if (status == 1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Comment for " + certificatetype);
                final View customLayout = requireActivity().getLayoutInflater().inflate(R.layout.list_comment, null);
                builder.setView(customLayout)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText txtComment = customLayout.findViewById(R.id.txt_comment);

                                String commenttext = txtComment.getText().toString().trim();
                                if (commenttext.isEmpty()) {
                                    Toast.makeText(getContext(), "Text field is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    Comment comment = new Comment(buildingcodetext,
                                            buildingcodetext + "_" + emailtext,
                                            buildingcodetext + "_" + certificatetype,
                                            commenttext,
                                            time);

                                    commentreference.push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getContext(), "Error sending " + certificatetype, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "Building not yet approved", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), certificatetype + " details have not been uploaded", Toast.LENGTH_LONG).show();
        }
    }

    private void download(String buildingcode, String certificate) {

        certificationreference.orderByChild("buildingcode_certificate").equalTo(buildingcode + "_" + certificate).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String url = "";

                if (snapshot != null) {


                    Certifications certifications = snapshot.getValue(Certifications.class);

                    url = certifications.getCertificateurl();

                    storageReference.child("BuildingCertifications/" + buildingcode + "/" + getFilename(Uri.parse(url))).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadFile(getContext(), buildingcode + "_" + certificate + " certificate", "pdf", "/BuildingCertifications", uri.toString());

                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Download successful");
                            builder.setMessage(buildingcode + "_" + certificate + " certificate" +".pdf");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error message : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

    private String getFilename(Uri uri) {
        File file = new File(uri.getPath());
        String displayName = file.getName();

        return displayName;
    }
}