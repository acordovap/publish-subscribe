package clients;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.UUID;

import javax.swing.DefaultListModel;

import interfaces.IClientPS;
import interfaces.IServerPS;
import objects.Publication;
import server.ServerPS;

public class GUIcPS extends javax.swing.JFrame {
	private static final long serialVersionUID = 7408963987148694415L;
	
	private CPS c;
	String saddress = "127.0.0.1"; // server's IP address
    IServerPS server; 
	
	public GUIcPS() {
        initComponents();
    }
    
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtxtMsg1 = new javax.swing.JTextArea();
        jpsswP = new javax.swing.JPasswordField();
        jtxtUser = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jbtnLogin = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jbtnSubscribe = new javax.swing.JButton();
        jbtnUnsubscribe = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtxtMsg = new javax.swing.JTextArea();
        jtxtToSU = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jbtnPublish = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtxtEvents = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jbtnRefresh = new javax.swing.JButton();

        jLabel3.setText("user:");

        jButton2.setText("login");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jtxtMsg1.setColumns(20);
        jtxtMsg1.setRows(5);
        jScrollPane4.setViewportView(jtxtMsg1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(400, 100));
        setResizable(false);

        jtxtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtUserActionPerformed(evt);
            }
        });

        jLabel1.setText("user:");

        jLabel2.setText("password:");

        jbtnLogin.setText("login");
        jbtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jbtnLoginActionPerformed(evt);
				} catch (RemoteException | NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setEnabled(false);
        jList1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jList1FocusGained(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel4.setText("Topics");

        jbtnSubscribe.setText("Subscribe");
        jbtnSubscribe.setEnabled(false);
        jbtnSubscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSubscribeActionPerformed(evt);
            }
        });

        jbtnUnsubscribe.setText("Unsubscribe");
        jbtnUnsubscribe.setEnabled(false);
        jbtnUnsubscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnUnsubscribeActionPerformed(evt);
            }
        });

        jtxtMsg.setColumns(20);
        jtxtMsg.setRows(5);
        jtxtMsg.setEnabled(false);
        jScrollPane2.setViewportView(jtxtMsg);

        jtxtToSU.setEnabled(false);
        jtxtToSU.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtToSUFocusGained(evt);
            }
        });

        jLabel5.setText("Selected Topic:");

        jLabel6.setText("Subscribed Topics");
        
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setEnabled(false);
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList2);

        jbtnPublish.setText("Publish");
        jbtnPublish.setEnabled(false);
        jbtnPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPublishActionPerformed(evt);
            }
        });

        jtxtEvents.setColumns(20);
        jtxtEvents.setRows(5);
        jtxtEvents.setFocusable(false);
        jScrollPane5.setViewportView(jtxtEvents);

        jLabel7.setText("Events");

        jbtnRefresh.setText("Refresh Topics");
        jbtnRefresh.setEnabled(false);
        jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(228, 228, 228)
                        .addComponent(jLabel6)
                        .addContainerGap(173, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3))
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(10, 10, 10)
                                .addComponent(jtxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jpsswP, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jbtnLogin)))
                        .addGap(15, 15, 15))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(jtxtToSU)))
                                .addGap(18, 18, 18)
                                .addComponent(jbtnSubscribe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnUnsubscribe))
                            .addComponent(jScrollPane5)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addGap(18, 18, 18)
                                .addComponent(jbtnPublish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jbtnLogin)
                    .addComponent(jLabel2)
                    .addComponent(jpsswP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 17, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtnUnsubscribe)
                            .addComponent(jbtnSubscribe)
                            .addComponent(jtxtToSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnPublish, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtUserActionPerformed

    private void jbtnLoginActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException, NotBoundException {//GEN-FIRST:event_jbtnLoginActionPerformed
        if(jbtnLogin.getText().equals("login")) {
        	if (jtxtUser.getText().equals("")) {
        		c = new CPS();
        	}
        	else {
        		c = new CPS(UUID.fromString(jtxtUser.getText()));
        	}
        	server = (IServerPS) LocateRegistry.getRegistry(saddress).lookup( ServerPS.class.getName());
        	server.register(c);
        	server.login(c);
            //if ( (!server.register(c) && server.login(c)) || (server.register(c) && server.login(c))) {
	        	jbtnLogin.setText("logout");
	            jtxtUser.setText(c.getUuid().toString()); jtxtUser.setEnabled(false);
	            jpsswP.setEnabled(false);
	
	            
	            DefaultListModel<String> listModel = new DefaultListModel<>();
	            String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
	            for (String car : cars) {
	                listModel.addElement(car);
	            }
	            //listModel.clear();
	            jList1.setModel(listModel);
	            
	            
	            /*jList1.removeAll(); */jList1.setEnabled(true);
	            /*jList2.removeAll(); */jList2.setEnabled(true);
	            jtxtToSU.setText(""); jtxtToSU.setEnabled(true);
	            jbtnRefresh.setEnabled(true);
	            jbtnSubscribe.setEnabled(true);
	            jbtnUnsubscribe.setEnabled(true);
	            jbtnPublish.setEnabled(true);
	            jtxtMsg.setText(""); jtxtMsg.setEnabled(true);
	            jtxtEvents.setText("");
            //}            
        }
        else{ //logout
            jbtnLogin.setText("login");
            jtxtUser.setEnabled(true);
            jpsswP.setEnabled(true);
            
            jList1.removeAll(); jList1.setEnabled(false);
            jList2.removeAll(); jList2.setEnabled(false);
            jtxtToSU.setText(""); jtxtToSU.setEnabled(false);
            jbtnRefresh.setEnabled(false);
            jbtnSubscribe.setEnabled(false);
            jbtnUnsubscribe.setEnabled(false);
            jbtnPublish.setEnabled(false);
            /*jtxtMsg.setText(""); */jtxtMsg.setEnabled(false);
            //jtxtEvents.setText("");


        }
            
            
    }//GEN-LAST:event_jbtnLoginActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jbtnSubscribeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSubscribeActionPerformed
        //String s = jtxtToSU.getText();
    }//GEN-LAST:event_jbtnSubscribeActionPerformed

    private void jbtnUnsubscribeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnUnsubscribeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnUnsubscribeActionPerformed

    private void jbtnPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPublishActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnPublishActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        jtxtToSU.setText(jList1.getSelectedValue());
    }//GEN-LAST:event_jList1ValueChanged

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        jtxtToSU.setText(jList2.getSelectedValue());
    }//GEN-LAST:event_jList2ValueChanged

    private void jList1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jList1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jList1FocusGained

    private void jtxtToSUFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtToSUFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtToSUFocusGained

    private void jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnRefreshActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIcPS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIcPS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIcPS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIcPS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GUIcPS().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jbtnLogin;
    private javax.swing.JButton jbtnPublish;
    private javax.swing.JButton jbtnRefresh;
    private javax.swing.JButton jbtnSubscribe;
    private javax.swing.JButton jbtnUnsubscribe;
    private javax.swing.JPasswordField jpsswP;
    private javax.swing.JTextArea jtxtEvents;
    private javax.swing.JTextArea jtxtMsg;
    private javax.swing.JTextArea jtxtMsg1;
    private javax.swing.JTextField jtxtToSU;
    private javax.swing.JFormattedTextField jtxtUser;
    // End of variables declaration//GEN-END:variables
    
    class CPS extends UnicastRemoteObject implements IClientPS, Serializable  {
    	private static final long serialVersionUID = 3631424906387316761L;
    	private UUID uuid;
    	private long currentTick;
    	
		protected CPS() throws RemoteException {
			super();
			currentTick = 0;
			uuid = UUID.randomUUID();
		}

		protected CPS(UUID uuid) throws RemoteException {
			super();
			currentTick = 0;
			this.uuid = uuid;
		}
		
		public long getCurrentTick() {
			return currentTick;
		}

		public void setCurrentTick(long currentTick) {
			this.currentTick = currentTick;
		}

		@Override
		public void notify(IClientPS c, String tn) throws RemoteException {
			System.out.println("Client with UUID: " + c.getUuid() + " subscribed to topic: " + tn);
		}
		
		@Override
		public void notify(Publication p, String tn) throws RemoteException {
			if (p.getTick() > getCurrentTick()) {
				setCurrentTick(p.getTick());
				System.out.println(tn + "\t--> " + "UUID: " + p.getUuidPublisher() + "\tMESSAGE: " + p.getMsg());
			}
		}

		@Override
		public UUID getUuid() throws RemoteException {
			return uuid;
		}

		@Override
		public PublicKey getPublicKey() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}
    	
    }
    
    
}


